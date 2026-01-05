import { User } from "../user/user.interface";

export interface Session {
  id?: number;
  name: string;
  description: string;
  date: Date;
  teacherId: number;
  users: User[];
  createdAt?: Date;
  updatedAt?: Date;
}
