import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../core/service/auth/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { expect } from '@jest/globals';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: Partial<AuthService>;
  let mockRouter: Partial<Router>;

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn().mockReturnValue(of(null))
    };

    mockRouter = {
      navigate: jest.fn().mockResolvedValue(true)
    };

    await TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RegisterComponent
      ],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form initially', () => {
    expect(component.form.valid).toBe(false);
  });

  it('should submit successfully and navigate to login', async () => {
    component.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: '123456'
    });

    await component.submit();
    await fixture.whenStable();

    expect(mockAuthService.register).toHaveBeenCalledWith({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: '123456'
    });

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should set onError true when register fails', async () => {
    (mockAuthService.register as jest.Mock).mockReturnValueOnce(
      throwError(() => new Error('Register failed'))
    );

    component.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: '123456'
    });

    await component.submit();
    await fixture.whenStable();

    expect(component.onError).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should mark form as invalid if fields are empty', async () => {
    component.form.setValue({
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    });

    expect(component.form.invalid).toBe(true);

    await component.submit();
    await fixture.whenStable();

    expect(mockAuthService.register).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should mark all fields as touched when submitting empty form', async () => {
    component.form.setValue({
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    });

    await component.submit();

    expect(component.form.touched).toBe(true);
    expect(component.form.controls.firstName.touched).toBe(true);
    expect(component.form.controls.lastName.touched).toBe(true);
    expect(component.form.controls.email.touched).toBe(true);
    expect(component.form.controls.password.touched).toBe(true);
  });

  it('should reactively update onError after failed submit', async () => {
    (mockAuthService.register as jest.Mock).mockReturnValueOnce(
      throwError(() => new Error('Error'))
    );

    component.form.setValue({
      firstName: 'Jane',
      lastName: 'Doe',
      email: 'jane.doe@example.com',
      password: '123456'
    });

    await component.submit();
    await fixture.whenStable();

    expect(component.onError).toBe(true);
  });

  describe('Integration tests (registration flow)', () => {

    it('should complete registration flow successfully', async () => {
      component.form.setValue({
        firstName: 'Alice',
        lastName: 'Smith',
        email: 'alice.smith@example.com',
        password: 'password123'
      });

      await component.submit();
      await fixture.whenStable();

      expect(mockAuthService.register).toHaveBeenCalledWith({
        firstName: 'Alice',
        lastName: 'Smith',
        email: 'alice.smith@example.com',
        password: 'password123'
      });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
      expect(component.onError).toBe(false);
    });

    it('should handle registration error correctly', async () => {
      (mockAuthService.register as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('Registration failed'))
      );

      component.form.setValue({
        firstName: 'Bob',
        lastName: 'Brown',
        email: 'bob.brown@example.com',
        password: 'password123'
      });

      await component.submit();
      await fixture.whenStable();

      expect(component.onError).toBe(true);
      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });

    it('should block submit when form is invalid', async () => {
      component.form.setValue({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
      });

      await component.submit();
      await fixture.whenStable();

      expect(mockAuthService.register).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
    });

  });
});
