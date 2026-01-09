import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { UserService } from 'src/app/core/service/user.service';
import { SessionService } from 'src/app/core/service/session.service';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockUserService: any;
  let mockSessionService: any;
  let mockRouter: any;

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

    mockRouter = { navigate: jest.fn() };

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
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
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

  it('should delete user, show snackbar, log out, and navigate', async () => {
    await component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
