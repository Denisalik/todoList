import type * as React from 'react';

interface StatusBadgeProps {
  active: boolean;
  activeLabel: string;
  inactiveLabel: string;
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ active, activeLabel, inactiveLabel }) => {
  const activeClassName = active
    ? 'bg-emerald-500/10 text-emerald-600'
    : 'bg-muted text-muted-foreground';
  const spanClassName = `inline-flex items-center rounded px-1.5 text-[11px] font-medium leading-4 ${activeClassName}`;
  return <span className={spanClassName}>{active ? activeLabel : inactiveLabel}</span>;
};
