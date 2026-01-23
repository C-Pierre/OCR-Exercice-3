import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { UserService } from 'src/app/core/service/user/user.service';
import { SessionService } from 'src/app/core/service/auth/session.service';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Component } from '@angular/core';

@Component({ template: '<p>Dummy</p>' })
class DummyComponent {}

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockUserService: any;
  let mockSessionService: any;
  let mockRouter: Router;

  const mockUser = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: false,
    password: 'secret',
    createdAt: new Date('2026-01-09T10:00:00Z'),
    updatedAt: new Date('2026-01-09T12:00:00Z')
  };

  beforeEach(async () => {
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(null))
    };

    mockSessionService = {
      sessionInformation: { id: 1, admin: false },
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        MeComponent
      ],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: { navigate: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    mockRouter = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set user$ on ngOnInit', async () => {
    component.ngOnInit();
    component.user$.subscribe(user => {
      expect(user).toEqual(mockUser);
      expect(mockUserService.getById).toHaveBeenCalledWith('1');
    });
  });

  it('should call window.history.back() when back() is called', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });

  it('should delete user, log out, and navigate', async () => {
    await component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

  describe('Integration tests (user flow)', () => {

    it('should fetch user and populate user$', async () => {
      component.ngOnInit();
      const user = await component.user$.toPromise();
      expect(user).toEqual(mockUser);
      expect(mockUserService.getById).toHaveBeenCalledWith('1');
    });

    it('should handle delete flow correctly', async () => {
      await component.delete();
      expect(mockUserService.delete).toHaveBeenCalledWith('1');
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });

    it('should handle errors when fetching user', async () => {
      (mockUserService.getById as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('User not found'))
      );

      component.ngOnInit();
      component.user$.subscribe({
        next: () => fail('should not succeed'),
        error: err => expect(err.message).toBe('User not found')
      });
    });

    it('should handle errors when deleting user', async () => {
      (mockUserService.delete as jest.Mock).mockReturnValueOnce(
        throwError(() => new Error('Delete failed'))
      );

      await component.delete().catch(err => {
        expect(err.message).toBe('Delete failed');
      });
    });

  });
});
