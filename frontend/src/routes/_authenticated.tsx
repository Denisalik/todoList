import { createFileRoute, redirect } from '@tanstack/react-router';
import { useStore } from '@/providers/store.ts';

export const Route = createFileRoute('/_authenticated')({
  beforeLoad: ({ location }) => {
    const { user, isAuthenticated, token } = useStore.getState().authentication;

    if (!user?.username || !isAuthenticated || !token) {
      throw redirect({
        to: '/login',
        search: { redirect: location.href },
      });
    }
  },
});
