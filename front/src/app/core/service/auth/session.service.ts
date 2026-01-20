import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SessionInformation } from '../../models/auth/sessionInformation.interface';

@Injectable({ providedIn: 'root' })
export class SessionService {

  private readonly STORAGE_KEY = 'session';

  private isLoggedSubject = new BehaviorSubject<boolean>(this.hasSession());
  public readonly isLogged$ = this.isLoggedSubject.asObservable();

  public get sessionInformation(): SessionInformation | undefined {
    const session = localStorage.getItem(this.STORAGE_KEY);
    return session ? JSON.parse(session) : undefined;
  }

  public logIn(session: SessionInformation): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(session));
    this.isLoggedSubject.next(true);
  }

  public logOut(): void {
    localStorage.removeItem(this.STORAGE_KEY);
    this.isLoggedSubject.next(false);
  }

  private hasSession(): boolean {
    return !!localStorage.getItem(this.STORAGE_KEY);
  }
}
