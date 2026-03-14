"use client";

import Link from "next/link";
import Image from "next/image";
import { useEffect, useState } from "react";
import styles from "./page.module.css";

export default function Home() {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    // Check login state initially
    const token = localStorage.getItem("token");
    if (token) setLoggedIn(true);

    // Listen for login/logout events
    const handleRoleChange = () => {
      const token = localStorage.getItem("token");
      setLoggedIn(!!token);
    };

    window.addEventListener("roleChange", handleRoleChange);

    // Cleanup listener
    return () => window.removeEventListener("roleChange", handleRoleChange);
  }, []);

  return (
    <div className={styles.container}>
      {/* Hero Section */}
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1>📇 Welcome to ContactsApp</h1>
          <p>
            Manage all your contacts in one place. Add, update, search, and organize your contacts effortlessly.
          </p>
          <div className={styles.ctaButtons}>
            {!loggedIn && (
              <>
                <Link href="/register" className={styles.primaryBtn}>Get Started</Link>
                <Link href="/login" className={styles.secondaryBtn}>Login</Link>
              </>
            )}
          </div>
        </div>
        <div className={styles.heroImage}>
          <Image
            src="/contact-illustration.png"
            alt="Contacts Illustration"
            width={400}
            height={400}
          />
        </div>
      </section>

      {/* Features Section */}
      <section className={styles.features}>
        <h2>Features</h2>
        <div className={styles.featureList}>
          <div className={styles.feature}>
            <h3>📝 Add Contacts</h3>
            <p>Easily add new contacts with detailed information.</p>
          </div>
          <div className={styles.feature}>
            <h3>🔍 Search & Filter</h3>
            <p>Quickly find contacts by name, company, city, or tags.</p>
          </div>
          <div className={styles.feature}>
            <h3>📊 Organize</h3>
            <p>Group, update, and manage your contacts effortlessly.</p>
          </div>
        </div>
      </section>

      {/* Footer CTA */}
      {!loggedIn && (
        <section className={styles.footerCTA}>
          <h2>Ready to manage your contacts?</h2>
          <Link href="/register" className={styles.primaryBtn}>Start Now</Link>
        </section>
      )}
    </div>
  );
}
