import { UserIcon } from '@hugeicons/core-free-icons';
import { HugeiconsIcon } from '@hugeicons/react';
import { createFileRoute } from '@tanstack/react-router';
import { StatusBadge } from '@/components/StatusBadge.tsx';
import { adminContextActions } from '@/providers/admin.ts';
import { useStore } from '@/providers/store.ts';
import { formatDate } from '@/util.ts';

export const Route = createFileRoute('/_authenticated/admin')({
  loader: async () => {
    await adminContextActions.getUsers();
  },
  component: RouteComponent,
});

function RouteComponent() {
  const { users } = useStore((state) => state.admin);
  if (!users.length) {
    return (
      <div className="flex flex-col items-center justify-center rounded-lg border bg-card py-10 text-center">
        <HugeiconsIcon icon={UserIcon} size={28} className="text-muted-foreground" />
        <p className="mt-2 text-muted-foreground text-sm">No users found</p>
      </div>
    );
  }
  return (
    <div className="overflow-x-auto rounded-lg border bg-card">
      <table className="w-full border-collapse text-sm">
        <thead>
          <tr className="border-b bg-muted/40 text-left font-medium text-xs">
            <th className="px-3 py-2 align-top">User</th>
            <th className="px-3 py-2 align-top">Email</th>
            <th className="px-3 py-2 align-top">Roles</th>
            <th className="px-3 py-2 align-top">Status</th>
            <th className="px-3 py-2 align-top">Version</th>
            <th className="px-3 py-2 align-top">Created</th>
            <th className="px-3 py-2 text-right align-top">ID</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr
              key={user.id!}
              className="border-b transition-colors last:border-0 hover:bg-muted/30"
            >
              <td className="px-3 py-2 align-top">
                <div className="flex items-center gap-2">
                  <span className="font-medium text-black leading-5">{user.username}</span>
                  {user.admin && (
                    <span className="inline-flex items-center rounded bg-primary/10 px-1.5 font-semibold text-[10px] text-primary uppercase leading-4">
                      Admin
                    </span>
                  )}
                </div>
              </td>
              <td className="px-3 py-2 text-left align-top">
                <span className="leading-5">{user.email ?? <span className="italic">—</span>}</span>
              </td>

              <td className="px-3 py-2 align-top">
                <div className="flex flex-wrap gap-1">
                  {user.roles?.length ? (
                    user.roles.map((role) => (
                      <span
                        key={role.id ?? role.name}
                        className="inline-flex items-center rounded bg-muted px-1.5 text-[11px] text-foreground leading-4"
                      >
                        {role.name}
                      </span>
                    ))
                  ) : (
                    <span className="italic leading-5">—</span>
                  )}
                </div>
              </td>

              <td className="px-3 py-2 align-top">
                <div className="flex flex-wrap gap-1">
                  <StatusBadge
                    active={user.enabled !== false}
                    activeLabel="Enabled"
                    inactiveLabel="Disabled"
                  />
                  {user.banned && (
                    <span className="inline-flex items-center rounded bg-destructive/10 px-1.5 font-medium text-[11px] text-destructive leading-4">
                      Banned
                    </span>
                  )}
                  {user.accountNonLocked === false && (
                    <span className="inline-flex items-center rounded bg-destructive/10 px-1.5 font-medium text-[11px] text-destructive leading-4">
                      Locked
                    </span>
                  )}
                </div>
              </td>
              <td className="px-3 py-2 text-left align-top font-mono text-xs leading-5">
                {user.version ?? '—'}
              </td>
              <td className="px-3 py-2 text-left align-top text-xs leading-5">
                {formatDate(user.createdDate)}
              </td>
              <td className="px-3 py-2 text-right align-top font-mono text-xs leading-5">
                {user.id ?? '—'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
