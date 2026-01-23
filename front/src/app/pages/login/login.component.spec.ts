import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { LoginComponent } from './login.component';
import { SessionService } from 'src/app/core/service/auth/session.service';
import { AuthService } from 'src/app/core/service/auth/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionInformation } from 'src/app/core/models/auth/sessionInformation.interface';
import { Component } from '@angular/core';

@Component({ template: '<p>Dummy</p>' })
class DummyComponent {}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let mockAuthService: Partial<AuthService>;
  let mockSessionService: Partial<SessionService>;
  let mockRouter: Router;

  const mockSession: SessionInformation = {
    token: 'jwt-token',
    type: 'user',
    id: 1,
    username: 'jdoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn().mockReturnValue(of(mockSession))
    };

    mockSessionService = {
      logIn: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DummyComponent }
        ]),
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        LoginComponent
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    mockRouter = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form when empty', () => {
    expect(component.form.valid).toBe(false);
  });

  it('should login successfully and redirect', async () => {
    component.form.setValue({ email: 'jdoe@example.com', password: '123456' });

    await component.submit();
    await fixture.whenStable();

    expect(mockAuthService.login).toHaveBeenCalledWith({
      email: 'jdoe@example.com',
      password: '123456'
    });

    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSession);
    expect(mockRouter.url).toBe('/sessions');
    expect(component.onError).toBe(false);
  });

  it('should set onError true when login fails', async () => {
    (mockAuthService.login as jest.Mock).mockReturnValueOnce(
      throwError(() => new Error('Login failed'))
    );

    component.form.setValue({ email: 'jdoe@example.com', password: 'wrongpass' });

    await component.submit();
    await fixture.whenStable();

    expect(component.onError).toBe(true);
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(mockRouter.url).not.toBe('/sessions');
  });

  describe('Integration tests (Router + login flow)', () => {

    it('should navigate to /sessions after successful login', async () => {
      component.form.setValue({ email: 'jdoe@example.com', password: '123456' });

      await component.submit();
      await fixture.whenStable();

      expect(component.onError).toBe(false);
      expect(mockRouter.url).toBe('/sessions');
    });

    it('should show error state on login failure', async () => {
      (mockAuthService.login as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('Login failed'))
      );

      component.form.setValue({ email: 'jdoe@example.com', password: 'wrongpass' });

      await component.submit();
      await fixture.whenStable();

      expect(component.onError).toBe(true);
      expect(mockRouter.url).not.toBe('/sessions');
    });

    it('should disable submit when form is invalid', () => {
      component.form.setValue({ email: '', password: '' });
      expect(component.form.valid).toBe(false);
    });

  });
});
