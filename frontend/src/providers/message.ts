import { toast } from 'sonner';

export const showMessage = {
  error: (description?: string) =>
    toast.error('Error happened!', {
      description,
      position: 'top-center',
      className:
        '!bg-red-50 !text-red-700 !border-red-200 ' +
        'dark:!bg-red-950 dark:!text-red-50 dark:!border-red-900',
      classNames: {
        description: '!text-red-700 dark:!text-red-200',
        icon: '!text-red-600 dark:!text-red-400',
      },
    }),

  success: (message: string, description?: string) =>
    toast.success(message, {
      description,
      position: 'top-center',
      className:
        '!bg-green-50 !text-green-900 !border-green-200 ' +
        'dark:!bg-green-950 dark:!text-green-50 dark:!border-green-900',
    }),
};
