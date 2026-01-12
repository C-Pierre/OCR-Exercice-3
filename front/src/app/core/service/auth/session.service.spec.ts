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

  it('should reflect hasSession() correctly', async () => {
    expect(service['hasSession']()).toBe(false);

    service.logIn(mockSession);
    expect(service['hasSession']()).toBe(true);

    service.logOut();
    expect(service['hasSession']()).toBe(false);
  });
});
