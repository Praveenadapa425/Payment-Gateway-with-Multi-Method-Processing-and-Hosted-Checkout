import React, { useState } from 'react';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // For this deliverable, we'll use the test merchant credentials
    if (email === 'test@example.com') {
      // Store mock authentication token
      localStorage.setItem('authToken', 'mock-token');
      // Redirect to dashboard
      window.location.href = '/dashboard';
    } else {
      alert('Invalid credentials. Use test@example.com as email.');
    }
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      backgroundColor: '#f8f9fa',
      padding: '20px'
    }}>
      <div style={{
        backgroundColor: 'white',
        padding: '40px',
        borderRadius: '10px',
        boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
        maxWidth: '400px',
        width: '100%'
      }}>
        <div style={{ textAlign: 'center', marginBottom: '30px' }}>
          <h2 style={{
            margin: 0,
            fontSize: '2rem',
            color: '#343a40'
          }}>Login</h2>
          <p style={{
            color: '#6c757d',
            marginTop: '8px',
            fontSize: '0.9rem'
          }}>Sign in to your dashboard</p>
        </div>
        <form data-test-id="login-form" onSubmit={handleSubmit}>
          <div style={{ marginBottom: '20px' }}>
            <label style={{
              display: 'block',
              marginBottom: '6px',
              fontWeight: '500',
              color: '#495057'
            }}>Email</label>
            <input
              data-test-id="email-input"
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={{
                width: '100%',
                padding: '12px',
                border: '1px solid #ced4da',
                borderRadius: '6px',
                fontSize: '1rem',
                transition: 'border-color 0.2s',
                boxSizing: 'border-box'
              }}
              onFocus={(e) => e.target.style.borderColor = '#007bff'}
              onBlur={(e) => e.target.style.borderColor = '#ced4da'}
            />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label style={{
              display: 'block',
              marginBottom: '6px',
              fontWeight: '500',
              color: '#495057'
            }}>Password</label>
            <input
              data-test-id="password-input"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{
                width: '100%',
                padding: '12px',
                border: '1px solid #ced4da',
                borderRadius: '6px',
                fontSize: '1rem',
                transition: 'border-color 0.2s',
                boxSizing: 'border-box'
              }}
              onFocus={(e) => e.target.style.borderColor = '#007bff'}
              onBlur={(e) => e.target.style.borderColor = '#ced4da'}
            />
          </div>
          <button
            data-test-id="login-button"
            type="submit"
            style={{
              width: '100%',
              padding: '14px',
              backgroundColor: '#007bff',
              color: 'white',
              border: 'none',
              cursor: 'pointer',
              borderRadius: '6px',
              fontWeight: '500',
              fontSize: '1.1rem',
              transition: 'background-color 0.2s'
            }}
            onMouseOver={(e) => e.target.style.backgroundColor = '#0069d9'}
            onMouseOut={(e) => e.target.style.backgroundColor = '#007bff'}
          >
            Sign In
          </button>
        </form>
        <div style={{
          marginTop: '20px',
          padding: '12px',
          backgroundColor: '#e7f3ff',
          borderRadius: '6px',
          border: '1px solid #b8daff',
          color: '#004085',
          fontSize: '0.9rem'
        }}>
          <strong>Test Credentials:</strong><br />
          Email: <code>test@example.com</code><br />
          Password: Any password accepted
        </div>
      </div>
    </div>
  );
}

export default Login;