import React from 'react';

function Success() {
  return (
    <div style={{ padding: '20px', textAlign: 'center' }}>
      <h2>Payment Successful!</h2>
      <p>Your payment has been processed successfully.</p>
      <button 
        onClick={() => window.location.href = '/checkout'}
        style={{ padding: '10px 20px', backgroundColor: '#28a745', color: 'white', border: 'none', cursor: 'pointer' }}
      >
        Make Another Payment
      </button>
    </div>
  );
}

export default Success;