import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../../models/auth/sessionInformation.interface';
import { firstValueFrom } from 'rxjs';
import { expect } from '@jest/globals';

describe('SessionService', () => {
  let service: SessionService;

  const STORAGE_KEY = 'session';

  const mockSession: SessionInformation = {
    token: 'jwt-token-123',
    type: 'user',
    id: 42,
    username: 'jdoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initially be logged out', async () => {
    const isLogged = await firstValueFrom(service.isLogged$);
    expect(isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should log in and store session', async () => {
    service.logIn(mockSession);

    const isLogged = await firstValueFrom(service.isLogged$);
    expect(isLogged).toBe(true);

    const stored = JSON.parse(localStorage.getItem(STORAGE_KEY) || '{}');
    expect(stored).toEqual(mockSession);

    expect(service.sessionInformation).toEqual(mockSession);
  });

  it('should log out and clear session', async () => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(mockSession));

    service.logOut();

    const isLogged = await firstValueFrom(service.isLogged$);
    expect(isLogged).toBe(false);

    expect(localStorage.getItem(STORAGE_KEY)).toBeNull();
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should reflect hasSession() correctly', () => {
    expect(service['hasSession']()).toBe(false);

    service.logIn(mockSession);
    expect(service['hasSession']()).toBe(true);

    service.logOut();
    expect(service['hasSession']()).toBe(false);
  });

  describe('Integration tests (state + localStorage)', () => {

    it('should restore session from localStorage on service creation', async () => {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(mockSession));

      TestBed.resetTestingModule();
      TestBed.configureTestingModule({});
      const reloadedService = TestBed.inject(SessionService);

      const isLogged = await firstValueFrom(reloadedService.isLogged$);
      expect(isLogged).toBe(true);
      expect(reloadedService.sessionInformation).toEqual(mockSession);
    });

    it('should emit logged-in state only once for multiple subscribers', async () => {
      const values1: boolean[] = [];
      const values2: boolean[] = [];

      service.isLogged$.subscribe(v => values1.push(v));
      service.isLogged$.subscribe(v => values2.push(v));

      service.logIn(mockSession);

      expect(values1).toEqual([false, true]);
      expect(values2).toEqual([false, true]);
    });

    it('should propagate logout to all subscribers', async () => {
      const values: boolean[] = [];
      service.isLogged$.subscribe(v => values.push(v));

      service.logIn(mockSession);
      service.logOut();

      expect(values).toEqual([false, true, false]);
    });

    it('should keep storage and state consistent after multiple logins', async () => {
      service.logIn(mockSession);
      service.logOut();

      service.logIn({ ...mockSession, id: 99 });

      const stored = JSON.parse(localStorage.getItem(STORAGE_KEY) || '{}');
      expect(stored.id).toBe(99);

      const isLogged = await firstValueFrom(service.isLogged$);
      expect(isLogged).toBe(true);
    });
  });
});
