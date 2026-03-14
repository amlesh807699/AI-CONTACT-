"use client";

import styles from "./page.module.css";
import { useState } from "react";
import { useRouter } from "next/navigation";

export default function RegisterPage() {
  const router = useRouter();

  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const res = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (res.status >= 500) throw new Error("Server error");

      const data = await res.text();
      if (!res.ok) {
        setMessage(data || "❌ Invalid input");
        return;
      }

      localStorage.setItem("verifyEmail", form.email);
      setForm({ name: "", email: "", password: "" });

      router.push("/verify");
    } catch (err) {
      throw new Error("Registration service unavailable");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Create Account ✨</h2>

      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="text"
          name="name"
          placeholder="Full Name"
          value={form.name}
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          required
          className={styles.input}
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          required
          className={styles.input}
        />

        <button type="submit" disabled={loading} className={styles.button}>
          {loading ? "Registering..." : "Register"}
        </button>
      </form>

      {message && <p className={styles.error}>{message}</p>}
    </div>
  );
}
