import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../../models/auth/loginRequest.interface';
import { RegisterRequest } from '../../models/auth/registerRequest.interface';
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

  describe('Integration tests (HTTP contract)', () => {

    it('should handle login API error (401)', () => {
      const payload: LoginRequest = {
        email: 'wrong@test.com',
        password: 'wrong'
      };

      service.login(payload).subscribe({
        next: () => fail('should not succeed'),
        error: error => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/login`);
      expect(req.request.method).toBe('POST');

      req.flush(
        { message: 'Invalid credentials' },
        { status: 401, statusText: 'Unauthorized' }
      );
    });

    it('should handle register API error (400)', () => {
      const payload: RegisterRequest = {
        firstName: '',
        lastName: '',
        email: 'invalid',
        password: '123'
      };

      service.register(payload).subscribe({
        next: () => fail('should not succeed'),
        error: error => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/register`);
      expect(req.request.method).toBe('POST');

      req.flush(
        { message: 'Validation error' },
        { status: 400, statusText: 'Bad Request' }
      );
    });

    it('should allow register then login flow', () => {
      const registerPayload: RegisterRequest = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@doe.com',
        password: '123456'
      };

      const loginPayload: LoginRequest = {
        email: 'john@doe.com',
        password: '123456'
      };

      const mockSession = {
        token: 'jwt-token',
        userId: 1
      };

      service.register(registerPayload).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const registerReq = httpMock.expectOne(`${baseUrl}/register`);
      expect(registerReq.request.method).toBe('POST');
      registerReq.flush(null);

      service.login(loginPayload).subscribe(session => {
        expect(session).toEqual(mockSession);
      });

      const loginReq = httpMock.expectOne(`${baseUrl}/login`);
      expect(loginReq.request.method).toBe('POST');
      loginReq.flush(mockSession);
    });
  });
});
