import { NextResponse } from "next/server";

export function middleware(req) {
  const token = req.cookies.get("token"); // read JWT cookie
  const url = req.nextUrl.clone();

  // Protect user/admin routes
  if (!token && url.pathname.startsWith("/user")) {
    url.pathname = "/login"; // redirect if not logged in
    return NextResponse.redirect(url);
  }

  return NextResponse.next(); // allow if token exists
}

export const config = {
  matcher: ["/user/:path*", "/admin/:path*"], // apply to protected routes
};
