import React, { useState } from "react";
import sha256 from "crypto-js/sha256";

const BRAND = "#8b5cf6";          
const BRAND_HOVER = "#7c3aed";     
const BORDER = "#e5e7eb";          
const TEXT = "#0f172a";            
const SUB = "#64748b";             
const ERROR_BG = "#fef2f2";
const ERROR_BR = "#fecaca";
const ERROR_TX = "#dc2626";
const FONT = "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif";

export default function SignUpForm({ onSwitchToLogin }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [pwd, setPwd] = useState("");
  const [confirm, setConfirm] = useState("");
  const [phone, setPhone] = useState("");
  const [department, setDepartment] = useState("");
  const [show1, setShow1] = useState(false);
  const [show2, setShow2] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const submit = async () => {
    setError("");

    // validation
    if (!name.trim() || !email.trim() || !pwd.trim() || !confirm.trim()) {
      setError("Please fill all required fields.");
      return;
    }
    if (pwd !== confirm) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);

    try {
      const hashedPassword = sha256(pwd).toString();
      const USERS_API_URL = "http://localhost:8080/users";

      // Send POST request to create new user
      const response = await fetch(USERS_API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },

        // The JSON key names match the backend SQL field variables
        body: JSON.stringify({
          name,
          email,
          passwordHash: hashedPassword,
          role: "CUSTOMER",
          phone,
          department
        }),
      });

      if (response.ok) {
        const newUser = await response.json();
        alert(`Account created for ${newUser.name} <${newUser.email}>`);
        onSwitchToLogin?.();
      } else {
        const errText = await response.text();
        setError("Signup failed: " + errText);
      }
    } catch (err) {
      console.error(err);
      setError("Network error");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => { if (e.key === "Enter") submit(); };

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
    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center", padding: 20, fontFamily: FONT, backgroundColor: "#f8fafc" }}>
      <div style={{ width: "100%", maxWidth: 480, backgroundColor: "#fff", borderRadius: 16, border: `1px solid ${BORDER}`, boxShadow: "0 10px 25px -5px rgba(0,0,0,.1), 0 4px 6px -2px rgba(0,0,0,.05)", padding: 32 }}>
        
        <div style={{ textAlign: "center", marginBottom: 20 }}>
          <h1 style={{ fontSize: 24, color: TEXT, margin: 0 }}>Create your account</h1>
          <p style={{ color: SUB, marginTop: 6 }}>It’s quick and easy.</p>
        </div>

        {error && (
          <div style={{ backgroundColor: ERROR_BG, border: `1px solid ${ERROR_BR}`, color: ERROR_TX, padding: "12px 16px", borderRadius: 8, marginBottom: 16, fontSize: 14 }}>
            {error}
          </div>
        )}

        <div style={{ display: "grid", gap: 16 }}>
          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Full name</label>
            <input value={name} onChange={(e)=>setName(e.target.value)} onKeyDown={handleKeyDown} placeholder="Enter your name" disabled={loading} style={inputBase} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Email address</label>
            <input type="email" value={email} onChange={(e)=>setEmail(e.target.value)} onKeyDown={handleKeyDown} placeholder="Enter your email" disabled={loading} style={inputBase} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Phone</label>
            <input type="tel" value={phone} onChange={(e)=>setPhone(e.target.value)} placeholder="Enter your phone number" disabled={loading} style={inputBase} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Department</label>
            <input value={department} onChange={(e)=>setDepartment(e.target.value)} placeholder="Enter your department" disabled={loading} style={inputBase} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Password</label>
            <div style={{ position: "relative" }}>
              <input type={show1 ? "text" : "password"} value={pwd} onChange={(e)=>setPwd(e.target.value)} onKeyDown={handleKeyDown} placeholder="Create a password" disabled={loading} style={{ ...inputBase, paddingRight: 48 }} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
              <button type="button" onClick={()=>setShow1(!show1)} disabled={loading} style={{ position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", color: "#9ca3af", padding: 4 }}>
                {show1 ? "X" : "[]"}
              </button>
            </div>
          </div>

          <div>
            <label style={{ display: "block", fontSize: 14, fontWeight: 500, color: "#374151", marginBottom: 6 }}>Confirm password</label>
            <div style={{ position: "relative" }}>
              <input type={show2 ? "text" : "password"} value={confirm} onChange={(e)=>setConfirm(e.target.value)} onKeyDown={handleKeyDown} placeholder="Re-enter password" disabled={loading} style={{ ...inputBase, paddingRight: 48 }} onFocus={(e)=> e.currentTarget.style.borderColor = BRAND} onBlur={(e)=> e.currentTarget.style.borderColor = BORDER} />
              <button type="button" onClick={()=>setShow2(!show2)} disabled={loading} style={{ position: "absolute", right: 12, top: "50%", transform: "translateY(-50%)", background: "none", border: "none", cursor: "pointer", color: "#9ca3af", padding: 4 }}>
                {show2 ? "X" : "[]"}
              </button>
            </div>
          </div>

          <button onClick={submit} disabled={loading} style={{ width: "100%", padding: "12px 16px", backgroundColor: loading ? "#9ca3af" : BRAND, color: "#fff", border: "none", borderRadius: 8, fontSize: 16, fontWeight: 600, cursor: loading ? "not-allowed" : "pointer", transition: "background-color .2s ease" }}
            onMouseEnter={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND_HOVER; }}
            onMouseLeave={(e)=>{ if(!loading) e.currentTarget.style.backgroundColor = BRAND; }}>
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
