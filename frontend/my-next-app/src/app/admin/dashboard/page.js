export default function Page() {
  return (
    <div className="min-h-screen bg-gray-100 p-6">
      {/* Header */}
      <header className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-800">
          Admin Dashboard
        </h1>
        <button className="rounded bg-red-500 px-4 py-2 text-white hover:bg-red-600">
          Logout
        </button>
      </header>

      {/* Stats */}
      <section className="mb-6 grid grid-cols-1 gap-6 md:grid-cols-4">
        <div className="rounded bg-white p-4 shadow">
          <h2 className="text-sm text-gray-500">Users</h2>
          <p className="text-2xl font-bold">1,245</p>
        </div>

        <div className="rounded bg-white p-4 shadow">
          <h2 className="text-sm text-gray-500">Orders</h2>
          <p className="text-2xl font-bold">312</p>
        </div>

        <div className="rounded bg-white p-4 shadow">
          <h2 className="text-sm text-gray-500">Revenue</h2>
          <p className="text-2xl font-bold">$12,430</p>
        </div>

        <div className="rounded bg-white p-4 shadow">
          <h2 className="text-sm text-gray-500">Pending Requests</h2>
          <p className="text-2xl font-bold">8</p>
        </div>
      </section>

      {/* Main content */}
      <section className="grid grid-cols-1 gap-6 md:grid-cols-2">
        {/* Recent Users */}
        <div className="rounded bg-white p-4 shadow">
          <h2 className="mb-4 text-lg font-semibold">Recent Users</h2>
          <ul className="space-y-2">
            <li className="flex justify-between border-b pb-2">
              <span>John Doe</span>
              <span className="text-sm text-gray-500">Admin</span>
            </li>
            <li className="flex justify-between border-b pb-2">
              <span>Jane Smith</span>
              <span className="text-sm text-gray-500">User</span>
            </li>
            <li className="flex justify-between">
              <span>Alex Brown</span>
              <span className="text-sm text-gray-500">User</span>
            </li>
          </ul>
        </div>

        {/* System Logs */}
        <div className="rounded bg-white p-4 shadow">
          <h2 className="mb-4 text-lg font-semibold">System Logs</h2>
          <ul className="space-y-2 text-sm text-gray-600">
            <li>✔ Server started</li>
            <li>✔ Database connected</li>
            <li>⚠ Password reset request</li>
            <li>✔ New user registered</li>
          </ul>
        </div>
      </section>
    </div>
  );
}
