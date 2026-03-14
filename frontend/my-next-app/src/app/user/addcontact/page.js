"use client";

import axios from "axios";
import { useState } from "react";
import { useRouter } from "next/navigation";
import styles from "./page.module.css";

const AddContactPage = () => {
  const router = useRouter();

  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    company: "",
    jobTitle: "",
    city: "",
    tags: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");
    setSuccess(false);

    try {
      await axios.post(
        "http://localhost:8080/user/contacts",
        { ...form, phone: Number(form.phone) },
        { withCredentials: true }
      );

      setMessage("✅ Contact added successfully");
      setSuccess(true);
      setTimeout(() => router.push("/user/allcontact"), 1500);

    } catch (err) {
      const status = err.response?.status;
      const data = err.response?.data;

      if (!err.response || status >= 500) {
        setMessage("❌ Contact service unavailable");
      } else if (status === 401) {
        setMessage("❌ Session expired. Please login again.");
        router.push("/login");
      } else if (status === 403) {
        setMessage("❌ You are not allowed to add contacts.");
      } else {
        setMessage("❌ " + (data || "Invalid input"));
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Add Contact 📇</h2>

      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          name="name"
          placeholder="Name"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="email"
          type="email"
          placeholder="Email"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="phone"
          placeholder="Phone (10 digits)"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="company"
          placeholder="Company"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="jobTitle"
          placeholder="Job Title"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="city"
          placeholder="City"
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          name="tags"
          placeholder="Tags (optional)"
          onChange={handleChange}
          className={styles.input}
        />

        <button type="submit" disabled={loading} className={styles.button}>
          {loading ? "Saving..." : "Add Contact"}
        </button>
      </form>

      {message && (
        <p className={`${styles.message} ${success ? styles.success : ""}`}>
          {message}
        </p>
      )}
    </div>
  );
};

export default AddContactPage;
