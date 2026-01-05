import React, { useState, useEffect } from 'react';

function Dashboard() {
  const [apiCredentials] = useState({
    apiKey: 'key_test_abc123',
    apiSecret: 'secret_test_xyz789'
  });
  
  const [stats, setStats] = useState({
    totalTransactions: 0,
    totalAmount: 0,
    successRate: 0
  });

  // Simulate fetching stats from API
  useEffect(() => {
    // In a real implementation, we would fetch this data from the backend
    // For now, we'll use mock data
    setStats({
      totalTransactions: 100,
      totalAmount: 500000,
      successRate: 95
    });
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Dashboard</h1>
      
      <div data-test-id="dashboard">
        <div data-test-id="api-credentials" style={{ marginBottom: '30px', padding: '15px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <h3>API Credentials</h3>
          <div>
            <label>API Key: </label>
            <span data-test-id="api-key">{apiCredentials.apiKey}</span>
          </div>
          <div>
            <label>API Secret: </label>
            <span data-test-id="api-secret">{apiCredentials.apiSecret}</span>
          </div>
        </div>
        
        <div data-test-id="stats-container" style={{ display: 'flex', gap: '20px', marginBottom: '30px' }}>
          <div data-test-id="total-transactions" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '5px', flex: 1 }}>
            <h3>Total Transactions</h3>
            <p>{stats.totalTransactions}</p>
          </div>
          <div data-test-id="total-amount" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '5px', flex: 1 }}>
            <h3>Total Amount</h3>
            <p>₹{stats.totalAmount.toLocaleString()}</p>
          </div>
          <div data-test-id="success-rate" style={{ padding: '15px', border: '1px solid #ddd', borderRadius: '5px', flex: 1 }}>
            <h3>Success Rate</h3>
            <p>{stats.successRate}%</p>
          </div>
        </div>
        
        <div>
          <a href="/dashboard/transactions" style={{ padding: '10px 15px', backgroundColor: '#007bff', color: 'white', textDecoration: 'none', borderRadius: '5px' }}>
            View Transactions
          </a>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;