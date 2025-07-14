import React from 'react';
import {
  List,
  ListItem,
  ListItemText,
  Button,
  Typography,
  Divider,
  Box,
} from '@mui/material';

function ShortUrlList({ results }) {
  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
  };

  return (
    <Box mt={4}>
      <Typography variant="h6" gutterBottom>
        Shortened URLs
      </Typography>
      <List>
        {results.map((res, idx) => (
          <React.Fragment key={idx}>
            <ListItem
              alignItems="flex-start"
              sx={{ display: 'flex', justifyContent: 'space-between' }}
            >
              {res.shortLink ? (
                <>
                  <ListItemText
                    primary={
                      <span>
                        <strong>Short Link:</strong>{' '}
                        <a
                          href={res.shortLink}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          {res.shortLink}
                        </a>
                      </span>
                    }
                    secondary={`Expires at: ${res.expiry || 'Never'}`}
                  />
                  <Button
                    variant="outlined"
                    onClick={() => handleCopy(res.shortLink)}
                  >
                    Copy
                  </Button>
                </>
              ) : (
                <ListItemText
                  primary={<span style={{ color: 'red' }}>Error: {res.error}</span>}
                />
              )}
            </ListItem>
            <Divider />
          </React.Fragment>
        ))}
      </List>
    </Box>
  );
}

export default ShortUrlList;
