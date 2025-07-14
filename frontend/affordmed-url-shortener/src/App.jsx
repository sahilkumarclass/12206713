import React, { useState } from 'react';
import { Container, Typography, Divider } from '@mui/material';
import UrlShortenerForm from './components/UrlShortenerForm';
import ShortUrlList from './components/ShortUrlList';
import AnalyticsView from './components/AnalyticsView';

function App() {
  const [results, setResults] = useState([]);

  return (
    <Container maxWidth="md">
      <Typography variant="h4" align="center" gutterBottom>
        Affordmed URL Shortener
      </Typography>
      <UrlShortenerForm onShorten={setResults} />
      <Divider sx={{ my: 4 }} />
      <ShortUrlList results={results} />
      <Divider sx={{ my: 4 }} />
      <AnalyticsView />
    </Container>
  );
}

export default App;
