"use client"; // ✅ MUST be at the very top

import { useEffect } from "react";
import { useRouter } from "next/navigation";
 // adjust the path if needed

export default function Error({ error, reset }) {
  const router = useRouter();

  useEffect(() => {
    console.error(error);
  }, [error]);

  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>Something went wrong!</h1>
      <p className={styles.error}>{error?.message || "An unexpected error occurred."}</p>
      <button
        className={styles.deleteButton}
        onClick={() => {
          reset();
          router.push("/admin/users"); // navigate back to users list
        }}
      >
        Go Back
      </button>
    </div>
  );
}
