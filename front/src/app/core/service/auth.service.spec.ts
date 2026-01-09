import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../models/loginRequest.interface';
import { RegisterRequest } from '../models/registerRequest.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/auth`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register user', () => {
    const payload: RegisterRequest = {
        firstName: 'test_test',
        lastName: 'test_test',
        email: 'test@test.com',
        password: '123456'
    };

    service.register(payload).subscribe(response => {
      expect(response).toBeUndefined;
    });

    const req = httpMock.expectOne(`${baseUrl}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(null);
  });

  it('should login user and return session information', () => {
    const payload: LoginRequest = {
      email: 'test@test.com',
      password: '123456'
    };

    const mockSession = {
      token: 'jwt-token',
      userId: 42
    };

    service.login(payload).subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`${baseUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(mockSession);
  });
});
