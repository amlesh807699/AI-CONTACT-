"use client";

import axios from "axios";
import { useState } from "react";
import styles from "./page.module.css"; // CSS Module

export default function SearchContactsPage() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [uiError, setUiError] = useState("");

  const searchContacts = async () => {
    if (!query.trim()) {
      setUiError("Please enter a search query");
      return;
    }

    setLoading(true);
    setUiError("");
    setResults([]);

    try {
      const res = await axios.get(
        "http://localhost:8080/user/contacts/search",
        {
          params: { query },
          withCredentials: true, 
        }
      );

      setResults(res.data);
    } catch (err) {
      console.error(err);

      if (!err.response || err.response.status >= 500) {
        throw new Error("Server error while searching contacts");
      }

      setUiError(err.response?.data?.message || "Search failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.searchContainer}>
      <h2>Search Contacts 🔍</h2>

      <div className={styles.searchBox}>
        <input
          type="text"
          placeholder="Search by name, company, city, tags..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button onClick={searchContacts} disabled={loading}>
          {loading ? "Searching..." : "Search"}
        </button>
      </div>

      {uiError && <p className={styles.errorText}>{uiError}</p>}

      {!loading && results.length === 0 && !uiError && (
        <p className={styles.emptyText}>No results found</p>
      )}

      <div className={styles.results}>
        {results.map((c) => (
          <div key={c.id} className={styles.contactCard}>
            <p><b>Name:</b> {c.name}</p>
            <p><b>Email:</b> {c.email}</p>
            <p><b>Phone:</b> {c.phone}</p>
            <p><b>Company:</b> {c.company}</p>
            <p><b>Job Title:</b> {c.jobTitle}</p>
            <p><b>City:</b> {c.city}</p>
            <p><b>Tags:</b> {c.tags}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
