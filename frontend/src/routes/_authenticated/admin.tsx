import {createFileRoute} from '@tanstack/react-router'
import {adminContextActions} from "@/providers/admin.ts";
import {StatusBadge} from "@/components/StatusBadge.tsx";
import {useStore} from "@/providers/store.ts";
import {formatDate} from "@/util.ts";
import {HugeiconsIcon} from "@hugeicons/react";
import {UserIcon} from "@hugeicons/core-free-icons";

export const Route = createFileRoute('/_authenticated/admin')({
  loader: async () => {
    await adminContextActions.getUsers();
  },
  component: RouteComponent,
})

function RouteComponent() {
  const {users} = useStore(state => state.admin);
  if (!users.length) {
    return (
      <div className="flex flex-col items-center justify-center rounded-lg border bg-card py-10 text-center">
        <HugeiconsIcon icon={UserIcon} size={28} className="text-muted-foreground"/>
        <p className="mt-2 text-sm text-muted-foreground">No users found</p>
      </div>
    );
  }
  return (
    <div className="overflow-x-auto rounded-lg border bg-card">
      <table className="w-full border-collapse text-sm">
        <thead>
        <tr className="border-b bg-muted/40 text-left text-xs font-medium">
          <th className="px-3 py-2 align-top">User</th>
          <th className="px-3 py-2 align-top">Email</th>
          <th className="px-3 py-2 align-top">Roles</th>
          <th className="px-3 py-2 align-top">Status</th>
          <th className="px-3 py-2 align-top">Version</th>
          <th className="px-3 py-2 align-top">Created</th>
          <th className="px-3 py-2 align-top text-right">ID</th>
        </tr>
        </thead>
        <tbody>
        {users.map((user) => (
          <tr
            key={user.id!}
            className="border-b last:border-0 transition-colors hover:bg-muted/30"
          >
            <td className="px-3 py-2 align-top">
              <div className="flex items-center gap-2">
                <span className="font-medium leading-5 text-black">{user.username}</span>
                {user.admin && (
                  <span className="inline-flex items-center rounded bg-primary/10 px-1.5 text-[10px] font-semibold uppercase leading-4 text-primary">
                    Admin
                  </span>
                )}
              </div>
            </td>
            <td className="px-3 py-2 align-top text-left">
              <span className="leading-5">
                {user.email ?? <span className="italic">—</span>}
              </span>
            </td>

            <td className="px-3 py-2 align-top">
              <div className="flex flex-wrap gap-1">
                {user.roles?.length ? (
                  user.roles.map((role) => (
                    <span
                      key={role.id ?? role.name}
                      className="inline-flex items-center rounded bg-muted px-1.5 text-[11px] leading-4 text-foreground"
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
                  <span className="inline-flex items-center rounded bg-destructive/10 px-1.5 text-[11px] font-medium leading-4 text-destructive">
                      Banned
                    </span>
                )}
                {user.accountNonLocked === false && (
                  <span className="inline-flex items-center rounded bg-destructive/10 px-1.5 text-[11px] font-medium leading-4 text-destructive">
                      Locked
                    </span>
                )}
              </div>
            </td>
            <td className="px-3 py-2 align-top font-mono text-xs leading-5 text-left">
              {user.version ?? '—'}
            </td>
            <td className="px-3 py-2 align-top text-xs leading-5 text-left">
              {formatDate(user.createdDate)}
            </td>
            <td className="px-3 py-2 align-top text-right font-mono text-xs leading-5">
              {user.id ?? '—'}
            </td>
          </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
}
