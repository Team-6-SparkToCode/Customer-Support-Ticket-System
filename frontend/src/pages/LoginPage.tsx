import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Navigate, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Select } from '../components/ui/Select';
import { Card, CardHeader, CardContent } from '../components/ui/Card';
import { MessageCircle } from 'lucide-react';
import toast from 'react-hot-toast';

const loginSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
  role: z.enum(['customer', 'staff', 'admin']),
});

type LoginForm = z.infer<typeof loginSchema>;

export function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  
  const from = (location.state as any)?.from?.pathname || null;

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: 'john@customer.com',
      password: 'password',
      role: 'customer',
    },
  });

  if (user) {
    const defaultRoute = user.role === 'customer' ? '/my/tickets' : '/staff/tickets';
    return <Navigate to={from || defaultRoute} replace />;
  }

  const onSubmit = async (data: LoginForm) => {
    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        throw new Error('Invalid credentials');
      }

      const result = await response.json();
      login(result.user);
      
      const defaultRoute = result.user.role === 'customer' ? '/my/tickets' : '/staff/tickets';
      navigate(from || defaultRoute, { replace: true });
      
      toast.success(`Welcome back, ${result.user.name}!`);
    } catch (error) {
      toast.error('Login failed. Please check your credentials.');
    }
  };

  return (
    <div className="min-h-screen bg-bg flex items-center justify-center p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            <MessageCircle className="h-12 w-12 text-blue-600" />
          </div>
          <h1 className="text-2xl font-bold text-ink">Support Portal</h1>
          <p className="text-muted">Sign in to your account</p>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <Input
              {...register('email')}
              type="email"
              label="Email"
              placeholder="Enter your email"
              error={errors.email?.message}
            />

            <Input
              {...register('password')}
              type="password"
              label="Password"
              placeholder="Enter your password"
              error={errors.password?.message}
            />

            <Select
              {...register('role')}
              label="Role (Dev Helper)"
              error={errors.role?.message}
            >
              <option value="customer">Customer</option>
              <option value="staff">Staff</option>
              <option value="admin">Admin</option>
            </Select>

            <Button
              type="submit"
              disabled={isSubmitting}
              className="w-full"
              size="lg"
            >
              {isSubmitting ? 'Signing in...' : 'Sign In'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}