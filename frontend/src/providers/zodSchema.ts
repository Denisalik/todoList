import { z } from 'zod';

export const loginSchema = z.object({
  username: z
    .string()
    .min(4, 'Username must be at least 4 characters')
    .max(32, 'Username must be at most 32 characters'),
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters')
    .max(32, 'Password must be at most 32 characters'),
});

export const registerSchema = z
  .object({
    username: z
      .string()
      .min(4, 'Username must be at least 4 characters')
      .max(32, 'Username must be at most 32 characters'),
    email: z
      .string()
      .min(6, 'Email must be at least 6 characters')
      .max(32, 'Email must be at most 32 characters')
      .pipe(z.email({ error: 'Invalid email address' })),
    password: z
      .string()
      .min(8, 'Password must be at least 8 characters')
      .max(32, 'Password must be at most 32 characters'),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  });

export type LoginFormValues = z.infer<typeof loginSchema>;
export type RegisterFormValues = z.infer<typeof registerSchema>;

export const updateToDoItemSchema = z.object({
  title: z.string().min(1, 'Title is required').max(255, 'Too long').optional(),
  description: z.string().min(1, 'Description is required').optional(),
});

export const createToDoItemSchema = z.object({
  title: z.string().min(1, 'Title is required').max(255, 'Too long'),
  description: z.string().min(1, 'Description is required'),
});

export type updateToDoItemValues = z.infer<typeof updateToDoItemSchema>;
export type createToDoItemValues = z.infer<typeof createToDoItemSchema>;
