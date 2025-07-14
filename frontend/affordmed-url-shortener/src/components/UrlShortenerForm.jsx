import React, { useState } from 'react';
import { TextField, Button, Grid, Typography } from '@mui/material';
import axios from 'axios';

function UrlShortenerForm({ onShorten }) {
  const [urls, setUrls] = useState([{ url: '', validity: '', shortcode: '' }]);
  const [errors, setErrors] = useState([]);

  const handleChange = (idx, field, value) => {
    const updated = [...urls];
    updated[idx][field] = value;
    setUrls(updated);
  };

  const handleAdd = () => {
    if (urls.length < 5)
      setUrls([...urls, { url: '', validity: '', shortcode: '' }]);
  };

  const validate = () => {
    const errs = urls.map(({ url, validity }) => {
      if (!/^https?:\/\/.+\..+/.test(url)) return 'Invalid URL';
      if (validity && !/^\d+$/.test(validity)) return 'Validity must be a number';
      return '';
    });
    setErrors(errs);
    return errs.every(e => !e);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const results = await Promise.all(
        urls.map(({ url, validity, shortcode }) =>
          axios
            .post(`http://localhost:8080/shorturls`, {
              url,
              validity: validity ? Number(validity) : undefined,
              shortcode: shortcode || undefined
            })
            .then(res => res.data)
            .catch(err => ({
              error: err.response?.data?.error || 'Unknown error'
            }))
        )
      );
      onShorten(results);
    } catch (err) {
      console.error('Unexpected error:', err);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Typography variant="h5" gutterBottom>
        Shorten up to 5 URLs
      </Typography>

      {urls.map((entry, idx) => (
        <Grid container spacing={2} alignItems="center" key={idx} sx={{ mb: 2 }}>
          <Grid item xs={4}>
            <TextField
              label="Long URL"
              value={entry.url}
              onChange={e => handleChange(idx, 'url', e.target.value)}
              required
              fullWidth
              error={Boolean(errors[idx])}
              helperText={errors[idx]}
            />
          </Grid>

          <Grid item xs={2}>
            <TextField
              label="Validity (min)"
              type="number"
              value={entry.validity}
              onChange={e => handleChange(idx, 'validity', e.target.value)}
              fullWidth
            />
          </Grid>

          <Grid item xs={3}>
            <TextField
              label="Custom Shortcode"
              value={entry.shortcode}
              onChange={e => handleChange(idx, 'shortcode', e.target.value)}
              fullWidth
            />
          </Grid>
        </Grid>
      ))}

      <Button onClick={handleAdd} disabled={urls.length >= 5} sx={{ mt: 2 }}>
        Add Another URL
      </Button>
      <Button type="submit" variant="contained" sx={{ mt: 2, ml: 2 }}>
        Shorten
      </Button>
    </form>
  );
}

export default UrlShortenerForm;
