import { TestBed } from '@angular/core/testing';
import { AuthGuard } from './auth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../core/service/auth/session.service';
import { BehaviorSubject, of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { expect } from '@jest/globals';

@Component({ standalone: true, template: '<p>dummy</p>' })
class DummyComponent {}

describe('AuthGuard', () => {
  describe('Unit tests', () => {
    let guard: AuthGuard;
    let router: Router;
    let isLogged$: BehaviorSubject<boolean>;

    beforeEach(() => {
      isLogged$ = new BehaviorSubject<boolean>(false);

      TestBed.configureTestingModule({
        providers: [
          AuthGuard,
          { provide: Router, useValue: { navigate: jest.fn() } },
          { provide: SessionService, useValue: { isLogged$ } }
        ]
      });

      guard = TestBed.inject(AuthGuard);
      router = TestBed.inject(Router);
    });

    it('should block access and redirect to / when not logged', async () => {
      isLogged$.next(false);

      const result = await guard.canActivate();

      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['login']);
    });

    it('should allow access when logged', async () => {
      isLogged$.next(true);

      const result = await guard.canActivate();

      expect(result).toBe(true);
      expect(router.navigate).not.toHaveBeenCalled();
    });
  });

  describe('Integration tests', () => {
    let router: Router;
    let location: Location;
    let mockSessionService: { isLogged$: BehaviorSubject<boolean> };

    beforeEach(async () => {
      mockSessionService = { isLogged$: new BehaviorSubject<boolean>(false) };

      await TestBed.configureTestingModule({
        imports: [
          DummyComponent,
          RouterTestingModule.withRoutes([
            { path: 'protected', component: DummyComponent, canActivate: [AuthGuard] },
            { path: '', component: DummyComponent }
          ])
        ],
        providers: [
          { provide: SessionService, useValue: mockSessionService }
        ]
      }).compileComponents();

      router = TestBed.inject(Router);
      location = TestBed.inject(Location);
    });

    it('should redirect to / when not logged', async () => {
      mockSessionService.isLogged$.next(false);

      await router.navigate(['protected']);
      expect(location.path()).toBe('/');
    });

    it('should allow navigation to protected route when logged', async () => {
      mockSessionService.isLogged$.next(true);

      await router.navigate(['protected']);
      expect(location.path()).toBe('/protected');
    });
  });
});