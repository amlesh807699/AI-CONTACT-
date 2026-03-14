"use client";

import axios from "axios";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "./page.module.css";

export default function ContactPage() {
  const { id } = useParams();
  const router = useRouter();

  const [contact, setContact] = useState(null);
  const [uiError, setUiError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    const controller = new AbortController();

    const fetchContact = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/user/contacts/${Number(id)}`,
          {
            withCredentials: true,
            signal: controller.signal,
          }
        );

        setContact(res.data);
      } catch (err) {
        if (axios.isCancel(err)) return;

        const status = err.response?.status;

        // SYSTEM ERRORS
        if (!status || status >= 500) {
          throw new Error("Failed to load contact details");
        }

        // UI ERRORS
        if (status === 401) {
          setUiError("Session expired. Please login again.");
        } else if (status === 403) {
          setUiError("You are not allowed to view this contact.");
        } else if (status === 404) {
          setUiError("Contact not found.");
        } else {
          setUiError("Something went wrong.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchContact();

    return () => controller.abort();
  }, [id]);

  if (loading) {
    return <p className={styles.loadingText}>Loading contact...</p>;
  }

  if (uiError) {
    return (
      <div className={styles.errorContainer}>
        <h2>{uiError}</h2>
        <button onClick={() => router.back()}>Go Back</button>
      </div>
    );
  }

  if (!contact) {
    return <p className={styles.emptyText}>No contact data available</p>;
  }

  return (
    <div className={styles.contactContainer}>
      <h1>Contact Details</h1>

      <div className={styles.contactCard}>
        <p><b>Name:</b> {contact.name}</p>
        <p><b>Email:</b> {contact.email}</p>
        <p><b>Phone:</b> {contact.phone}</p>
        <p><b>Company:</b> {contact.company}</p>
        <p><b>Job Title:</b> {contact.jobTitle}</p>
        <p><b>City:</b> {contact.city}</p>
        <p><b>Tags:</b> {contact.tags}</p>
      </div>
    </div>
  );
}
