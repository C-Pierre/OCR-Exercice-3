import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs';
import { Component } from '@angular/core';
import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../core/service/session.service';
import { expect } from '@jest/globals';

@Component({ standalone: true, template: '<p>dummy</p>' })
class DummyComponent {}

describe('UnauthGuard', () => {
  let guard: UnauthGuard;
  let router: Router;
  let location: Location;
  let isLogged$: BehaviorSubject<boolean>;

  beforeEach(async () => {
    isLogged$ = new BehaviorSubject<boolean>(false);

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'unauth', component: DummyComponent, canActivate: [UnauthGuard] },
          { path: '', component: DummyComponent }
        ]),
        DummyComponent
      ],
      providers: [
        UnauthGuard,
        { provide: SessionService, useValue: { isLogged$ } }
      ]
    }).compileComponents();

    guard = TestBed.inject(UnauthGuard);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should block access and redirect to / if logged', async () => {
    isLogged$.next(true);
    const spy = jest.spyOn(router, 'navigate');

    const result = await guard.canActivate();

    expect(result).toBe(false);
    expect(spy).toHaveBeenCalledWith(['/']);
  });

  it('should allow access if not logged', async () => {
    isLogged$.next(false);
    const spy = jest.spyOn(router, 'navigate');

    const result = await guard.canActivate();

    expect(result).toBe(true);
    expect(spy).not.toHaveBeenCalled();
  });

  it('should redirect to / if user is logged and tries to access unauth route', async () => {
    isLogged$.next(true);
    await router.navigate(['/unauth']);
    expect(location.path()).toBe('/');
  });

  it('should allow navigation to unauth route when user is not logged', async () => {
    isLogged$.next(false);
    await router.navigate(['/unauth']);
    expect(location.path()).toBe('/unauth');
  });
});
