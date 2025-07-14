import React, { useState } from 'react';
import {
  TextField,
  Button,
  Typography,
  List,
  ListItem,
  ListItemText,
  Box,
  Divider,
} from '@mui/material';
import axios from 'axios';

function AnalyticsView() {
  const [shortcode, setShortcode] = useState('');
  const [stats, setStats] = useState(null);
  const [error, setError] = useState('');

  const fetchStats = async () => {
    setError('');
    setStats(null);
    try {
      const res = await axios.get(`http://localhost:8080/shorturls/stats/${shortcode}`);
      setStats(res.data);
    } catch (err) {
      setError(err.response?.data?.error || 'Shortcode not found');
    }
  };

  return (
    <Box p={4}>
      <Typography variant="h6" gutterBottom>
        View Analytics
      </Typography>
      <Box display="flex" alignItems="center" mb={2}>
        <TextField
          label="Shortcode"
          value={shortcode}
          onChange={e => setShortcode(e.target.value)}
          sx={{ mr: 2 }}
        />
        <Button variant="contained" onClick={fetchStats}>
          Fetch
        </Button>
      </Box>

      {error && <Typography color="error">{error}</Typography>}

      {stats && (
        <Box mt={3}>
          <Typography>
            <strong>Original URL:</strong> {stats.originalUrl || 'N/A'}
          </Typography>
          <Typography>
            <strong>Short Link:</strong>{' '}
            <a href={stats.shortLink} target="_blank" rel="noopener noreferrer">
              {stats.shortLink}
            </a>
          </Typography>
          <Typography>
            <strong>Created:</strong> {stats.createdAt || 'N/A'}
          </Typography>
          <Typography>
            <strong>Expires:</strong> {stats.expireAt || 'N/A'}
          </Typography>
          <Typography>
            <strong>Total Clicks:</strong> {stats.clickCount ?? 0}
          </Typography>

          <Box mt={2}>
            <Typography variant="subtitle1" gutterBottom>
              Click Details:
            </Typography>
            <List>
              {stats.clickDetails && stats.clickDetails.length > 0 ? (
                stats.clickDetails.map((cd, idx) => (
                  <React.Fragment key={idx}>
                    <ListItem>
                      <ListItemText
                        primary={`Clicked at: ${cd.timestamp}`}
                        secondary={`Referrer: ${cd.referrer || 'N/A'}, Geo: ${cd.geoLocation || 'N/A'}`}
                      />
                    </ListItem>
                    <Divider />
                  </React.Fragment>
                ))
              ) : (
                <ListItem>
                  <ListItemText primary="No click activity yet." />
                </ListItem>
              )}
            </List>
          </Box>
        </Box>
      )}
    </Box>
  );
}

export default AnalyticsView;
