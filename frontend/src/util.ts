export const isNull = <T>(value: T | undefined | null): value is undefined | null => value === null || value === undefined;
export const isNonNull = <T>(value: T): value is NonNullable<T> => value !== null && value !== undefined;

export const getCookie = (name: string) => {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  return match ? decodeURIComponent(match[2]) : null;
}
export const formatDate = (value?: string): string => {
  if (!value) return '—';
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return value; // show raw if unparseable
  return d.toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: '2-digit',
  });
}