import React from 'react';

function Failure() {
  return (
    <div style={{ padding: '20px', textAlign: 'center' }}>
      <h2>Payment Failed</h2>
      <p>Sorry, your payment could not be processed.</p>
      <button 
        onClick={() => window.location.href = '/checkout'}
        style={{ padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', cursor: 'pointer' }}
      >
        Try Again
      </button>
    </div>
  );
}

export default Failure;