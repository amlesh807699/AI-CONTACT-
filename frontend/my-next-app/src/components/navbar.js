"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "./navbar.module.css";

export default function Navbar() {
  const router = useRouter();
  const pathname = usePathname();
  const [userRole, setUserRole] = useState(null);

  useEffect(() => {
    // Initial role
    setUserRole(localStorage.getItem("role"));

    // Listen to custom event
    const handleRoleChange = () => {
      setUserRole(localStorage.getItem("role"));
    };

    window.addEventListener("roleChange", handleRoleChange);

    return () => {
      window.removeEventListener("roleChange", handleRoleChange);
    };
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("role");
    localStorage.removeItem("email");

    // Dispatch event so navbar updates
    window.dispatchEvent(new Event("roleChange"));

    router.push("/login");
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles["navbar-logo"]}>
        <Link href="/">📇 ContactsApp</Link>
      </div>

      <ul className={styles["navbar-links"]}>
        <li>
          <Link href="/" className={pathname === "/" ? styles.active : ""}>Home</Link>
        </li>

        {userRole === "USER" && (
          <>
            <li>
              <Link href="/user/dashboard" className={pathname.startsWith("/user") ? styles.active : ""}>Dashboard</Link>
            </li>
            <li>
              <Link href="/user/allcontact" className={pathname.startsWith("/user/allcontact") ? styles.active : ""}>Contacts</Link>
            </li>
            <li>
              <Link href="/user/addcontact" className={pathname.startsWith("/user/addcontact") ? styles.active : ""}>Add Contact</Link>
            </li>
            <li>
              <Link href="/user/search" className={pathname.startsWith("/user/search") ? styles.active : ""}>Search</Link>
            </li>
          </>
        )}

        {userRole === "ADMIN" && (
          <li>
            <Link href="/admin/dashboard" className={pathname.startsWith("/admin") ? styles.active : ""}>Admin</Link>
          </li>
        )}

        {!userRole ? (
          <>
            <li>
              <Link href="/login" className={pathname === "/login" ? styles.active : ""}>Login</Link>
            </li>
            <li>
              <Link href="/register" className={pathname === "/register" ? styles.active : ""}>Register</Link>
            </li>
          </>
        ) : (
          <li>
            <button onClick={handleLogout} className={styles["logout-btn"]}>Logout</button>
          </li>
        )}
      </ul>
    </nav>
  );
}
