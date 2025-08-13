import React, { useState } from "react";

// Single file with LOGIN + SIGNUP views (no router needed)
// Click "Sign up" or "Log in" links to switch.

export default function LoginPage() {
  const [view, setView] = useState("login"); // 'login' | 'signup'
  return view === "login" ? (
    <LoginForm onSwitchToSignUp={() => setView("signup")} />
  ) : (
    <SignUpForm onSwitchToLogin={() => setView("login")} />
  );
}

// ===== Shared palette =====
const BRAND = "#8b5cf6";          // purple
const BRAND_HOVER = "#7c3aed";     // darker purple
const BORDER = "#e5e7eb";          // neutral border
const TEXT = "#0f172a";            // title text
const SUB = "#64748b";             // muted text
const ERROR_BG = "#fef2f2";
const ERROR_BR = "#fecaca";
const ERROR_TX = "#dc2626";
const FONT = "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

// ===== LOGIN =====
function LoginForm({ onSwitchToSignUp }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [remember, setRemember] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    setError("");
    if (!email.trim()) { setError("Email is required"); return; }
    if (!password.trim()) { setError("Password is required"); return; }
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      alert(`Logged in as: ${email}`);
    }, 1200);
  };

  const handleKeyDown = (e) => { if (e.key === 'Enter') handleSubmit(); };

  const inputBase = {
    width: "100%",
    padding: "12px 16px",
    fontSize: "16px",
    border: `2px solid ${BORDER}`,
    borderRadius: "8px",
    outline: "none",
    backgroundColor: "#ffffff",
    color: "#111827",
    boxSizing: "border-box",
    transition: "border-color .2s ease",
  };

  return (
    <div style={{
      minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center",
      padding: 20, backgroundColor: "#f8fafc", fontFamily: FONT
    }}>
      <div style={{ width: "100%", maxWidth: 460, backgroundColor: "#fff", borderRadius: 16,
        border: `1px solid #e2e8f0`, boxShadow: "0 10px 25px -5px rgba(0,0,0,.1), 0 4px 6px -2px rgba(0,0,0,.05)", padding: 32 }}>

        {/* Header with brand */}
        <div style={{ textAlign: "center", marginBottom: 24 }}>
          <div style={{ display: "inline-flex", alignItems: "center", gap: 10, marginBottom: 8 }}>
            <div style={{ width: 40, height: 40, backgroundColor: BRAND, borderRadius: 10, display: "grid", placeItems: "center" }}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M12 2 2 7l10 5 10-5-10-5Z M2 12l10 5 10-5 M2 17l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
            <strong style={{ fontSize: 18, color: TEXT }}>SparkSupport</strong>
          </div>
          <div style={{ fontSize: 28, marginBottom: 6 }}>👋</div>
          <p style={{ color: SUB, margin: 0 }}>Please sign in to your account</p>
        </div>

        {error && (
          <div style={{ backgroundColor: ERROR_BG, border: `1px solid ${ERROR_BR}`, color: ERROR_TX,
            padding: "12px 16px", borderRadius: 8, marginBottom: 16, fontSize: 14 }}>{error}</div>
        )}

        <div style={{ display: "grid", gap: 16 }}>
          {/* Email */}
          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Email address</label>
            <input type="email" value={email} onChange={(e)=>setEmail(e.target.value)} onKeyDown={handleKeyDown}
              placeholder="Enter your email" disabled={loading}
              style={inputBase}
              onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
              onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
            />
          </div>

          {/* Password */}
          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Password</label>
            <div style={{ position: "relative" }}>
              <input type={showPassword ? "text" : "password"} value={password} onChange={(e)=>setPassword(e.target.value)} onKeyDown={handleKeyDown}
                placeholder="Enter your password" disabled={loading}
                style={{ ...inputBase, paddingRight: 48 }}
                onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
                onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
              />
              <button type="button" onClick={()=>setShowPassword(!showPassword)} disabled={loading}
                style={{ position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", color: "#9ca3af", padding: 4 }}>
                {showPassword ? (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" stroke="currentColor" strokeWidth="2"/>
                    <line x1="1" y1="1" x2="23" y2="23" stroke="currentColor" strokeWidth="2"/>
                  </svg>
                ) : (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke="currentColor" strokeWidth="2"/>
                    <circle cx="12" cy="12" r="3" stroke="currentColor" strokeWidth="2"/>
                  </svg>
                )}
              </button>
            </div>
          </div>

          {/* Remember + forgot */}
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: 14 }}>
            <label style={{ display: "flex", alignItems: "center", gap: 8, cursor: "pointer", color: "#4b5563" }}>
              <input type="checkbox" checked={remember} onChange={(e)=>setRemember(e.target.checked)} disabled={loading} style={{ accentColor: BRAND }} />
              Remember me
            </label>
            <a href="#" style={{ color: BRAND, textDecoration: "none", fontWeight: 500 }}>Forgot password?</a>
          </div>

          {/* Submit */}
          <button onClick={handleSubmit} disabled={loading}
            style={{ width: "100%", padding: "12px 16px", backgroundColor: loading ? "#9ca3af" : BRAND, color: "#fff", border: "none", borderRadius: 8, fontSize: 16, fontWeight: 600, cursor: loading ? "not-allowed" : "pointer", transition: "background-color .2s ease" }}
            onMouseEnter={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND_HOVER; }}
            onMouseLeave={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND; }}
          >
            {loading ? "Signing in..." : "Sign in"}
          </button>
        </div>

        {/* Footer switch */}
        <div style={{ textAlign: "center", marginTop: 24, fontSize: 14 }}>
          <span style={{ color: SUB }}>Don't have an account? </span>
          <a href="#" onClick={(e)=>{ e.preventDefault(); onSwitchToSignUp?.(); }} style={{ color: BRAND, textDecoration: "none", fontWeight: 500 }}>Sign up</a>
        </div>
      </div>
    </div>
  );
}

// ===== SIGN UP =====
function SignUpForm({ onSwitchToLogin }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [pwd, setPwd] = useState("");
  const [confirm, setConfirm] = useState("");
  const [show1, setShow1] = useState(false);
  const [show2, setShow2] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const submit = () => {
    setError("");
    if (!name.trim() || !email.trim() || !pwd.trim() || !confirm.trim()) { setError("Please fill all fields."); return; }
    if (pwd !== confirm) { setError("Passwords do not match."); return; }
    setLoading(true);
    setTimeout(()=>{ setLoading(false); alert(`Account created for ${name} <${email}>`); onSwitchToLogin?.(); }, 1200);
  };

  const handleKeyDown = (e) => { if (e.key === 'Enter') submit(); };

  const inputBase = {
    width: "100%",
    padding: "12px 16px",
    fontSize: "16px",
    border: `2px solid ${BORDER}`,
    borderRadius: "8px",
    outline: "none",
    backgroundColor: "#ffffff",
    color: "#111827",
    boxSizing: "border-box",
    transition: "border-color .2s ease",
  };

  return (
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", padding: 20, backgroundColor: "#f8fafc", fontFamily: FONT }}>
      <div style={{ width: "100%", maxWidth: 480, backgroundColor: "#fff", borderRadius: 16, border: `1px solid #e2e8f0`, boxShadow: "0 10px 25px -5px rgba(0,0,0,.1), 0 4px 6px -2px rgba(0,0,0,.05)", padding: 32 }}>
        <div style={{ textAlign: "center", marginBottom: 20 }}>
          <div style={{ display: "inline-flex", alignItems: "center", gap: 10, marginBottom: 8 }}>
            <div style={{ width: 40, height: 40, backgroundColor: BRAND, borderRadius: 10, display: "grid", placeItems: "center" }}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M12 2 2 7l10 5 10-5-10-5Z M2 12l10 5 10-5 M2 17l10 5 10-5" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </div>
            <strong style={{ fontSize: 18, color: TEXT }}>SparkSupport</strong>
          </div>
          <h1 style={{ fontSize: 24, color: TEXT, margin: 0 }}>Create your account</h1>
          <p style={{ color: SUB, margin: "6px 0 0" }}>It’s quick and easy.</p>
        </div>

        {error && (
          <div style={{ backgroundColor: ERROR_BG, border: `1px solid ${ERROR_BR}`, color: ERROR_TX,
            padding: "12px 16px", borderRadius: 8, marginBottom: 16, fontSize: 14 }}>{error}</div>
        )}

        <div style={{ display: "grid", gap: 16 }}>
          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Full name</label>
            <input value={name} onChange={(e)=>setName(e.target.value)} onKeyDown={handleKeyDown}
              placeholder="Enter your name" disabled={loading}
              style={inputBase}
              onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
              onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
            />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Email address</label>
            <input type="email" value={email} onChange={(e)=>setEmail(e.target.value)} onKeyDown={handleKeyDown}
              placeholder="Enter your email" disabled={loading}
              style={inputBase}
              onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
              onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
            />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Password</label>
            <div style={{ position: "relative" }}>
              <input type={show1 ? "text" : "password"} value={pwd} onChange={(e)=>setPwd(e.target.value)} onKeyDown={handleKeyDown}
                placeholder="Create a password" disabled={loading}
                style={{ ...inputBase, paddingRight: 48 }}
                onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
                onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
              />
              <button type="button" onClick={()=>setShow1(!show1)} disabled={loading}
                style={{ position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", color: "#9ca3af", padding: 4 }}>
                {show1 ? (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" stroke="currentColor" strokeWidth="2"/><line x1="1" y1="1" x2="23" y2="23" stroke="currentColor" strokeWidth="2"/></svg>
                ) : (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke="currentColor" strokeWidth="2"/><circle cx="12" cy="12" r="3" stroke="currentColor" strokeWidth="2"/></svg>
                )}
              </button>
            </div>
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Confirm password</label>
            <div style={{ position: "relative" }}>
              <input type={show2 ? "text" : "password"} value={confirm} onChange={(e)=>setConfirm(e.target.value)} onKeyDown={handleKeyDown}
                placeholder="Re-enter password" disabled={loading}
                style={{ ...inputBase, paddingRight: 48 }}
                onFocus={(e)=> e.currentTarget.style.borderColor = BRAND}
                onBlur={(e)=> e.currentTarget.style.borderColor = BORDER}
              />
              <button type="button" onClick={()=>setShow2(!show2)} disabled={loading}
                style={{ position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", color: "#9ca3af", padding: 4 }}>
                {show2 ? (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" stroke="currentColor" strokeWidth="2"/><line x1="1" y1="1" x2="23" y2="23" stroke="currentColor" strokeWidth="2"/></svg>
                ) : (
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke="currentColor" strokeWidth="2"/><circle cx="12" cy="12" r="3" stroke="currentColor" strokeWidth="2"/></svg>
                )}
              </button>
            </div>
          </div>

          <button onClick={submit} disabled={loading}
            style={{ width: "100%", padding: "12px 16px", backgroundColor: loading ? "#9ca3af" : BRAND, color: "#fff", border: "none", borderRadius: 8, fontSize: 16, fontWeight: 600, cursor: loading ? "not-allowed" : "pointer", transition: "background-color .2s ease" }}
            onMouseEnter={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND_HOVER; }}
            onMouseLeave={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND; }}
          >
            {loading ? "Creating..." : "Create account"}
          </button>
        </div>

        <div style={{ textAlign: "center", marginTop: 24, fontSize: 14 }}>
          <span style={{ color: SUB }}>Already have an account? </span>
          <a href="#" onClick={(e)=>{ e.preventDefault(); onSwitchToLogin?.(); }} style={{ color: BRAND, textDecoration: "none", fontWeight: 500 }}>Log in</a>
        </div>
      </div>
    </div>
  );
}
