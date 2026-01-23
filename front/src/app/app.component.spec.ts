import { TestBed, ComponentFixture } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { AppComponent } from './app.component';
import { SessionService } from './core/service/auth/session.service';
import { AuthService } from './core/service/auth/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of, BehaviorSubject } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { MatToolbarModule } from '@angular/material/toolbar';
import { By } from '@angular/platform-browser';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let isLogged$: BehaviorSubject<boolean>;

  const mockSessionService = {
    isLogged$: null as unknown as BehaviorSubject<boolean>,
    logOut: jest.fn(),
  };

  const mockAuthService = {};

  const mockRouter = {
    navigate: jest.fn(),
  };

  beforeEach(async () => {
    isLogged$ = new BehaviorSubject<boolean>(true);
    mockSessionService.isLogged$ = isLogged$;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule,
        AppComponent,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => null } },
            root: {}
          }
        }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should return isLogged observable from SessionService', (done) => {
    component.isLogged.subscribe((value) => {
      expect(value).toBe(true);
      done();
    });
  });

  it('should call logOut and navigate on logout', () => {
    component.logout();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });

  describe('Integration tests (reactive flows + UI)', () => {
    it('should react to isLogged$ changes', (done) => {
      let values: boolean[] = [];
      component.isLogged.subscribe(value => {
        values.push(value);
        if (values.length === 2) {
          expect(values).toEqual([true, false]);
          done();
        }
      });

      isLogged$.next(false);
    });

    it('should navigate to home on logout click', () => {
      component.logout();
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
    });

    it('should handle multiple isLogged$ emissions', (done) => {
      let emitted: boolean[] = [];
      component.isLogged.subscribe(v => {
        emitted.push(v);
        if (emitted.length === 3) {
          expect(emitted).toEqual([true, false, true]);
          done();
        }
      });

      isLogged$.next(false);
      isLogged$.next(true);
    });
  });
});
