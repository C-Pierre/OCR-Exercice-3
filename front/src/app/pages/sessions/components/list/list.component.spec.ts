import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../../../core/service/session/session-api.service';
import { SessionService } from '../../../../core/service/auth/session.service';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessions = [
    { id: 1, name: 'Yoga', description: 'Session desc', date: new Date(), teacherId: 10, users: [], createdAt: new Date(), updatedAt: new Date() },
    { id: 2, name: 'Pilates', description: 'Another desc', date: new Date(), teacherId: 11, users: [], createdAt: new Date(), updatedAt: new Date() }
  ];

  const mockUser = {
    id: 1,
    admin: true,
    username: 'john',
    firstName: 'John',
    lastName: 'Doe',
    type: 'user',
    token: '123'
  };

  let mockSessionApiService: any;
  let mockSessionService: any;

  beforeEach(async () => {
    mockSessionApiService = {
      all: jest.fn().mockReturnValue(of(mockSessions))
    };

    mockSessionService = {
      sessionInformation: mockUser,
      isLogged$: of(true)
    };

    await TestBed.configureTestingModule({
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule, ListComponent],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load sessions$', (done) => {
    component.sessions$.subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
      expect(mockSessionApiService.all).toHaveBeenCalled();
      done();
    });
  });

  it('should load user$', (done) => {
    component.user$.subscribe(user => {
      expect(user).toEqual(mockUser);
      done();
    });
  });

  describe('Integration tests (full flow)', () => {
    it('should display all sessions from sessions$', (done) => {
      component.sessions$.subscribe(sessions => {
        expect(sessions.length).toBe(2);
        expect(sessions[0].name).toBe('Yoga');
        expect(sessions[1].name).toBe('Pilates');
        done();
      });
    });

    it('should expose current user from user$', (done) => {
      component.user$.subscribe(user => {
        expect(user?.admin).toBe(true);
        expect(user?.firstName).toBe('John');
        done();
      });
    });

    it('should reactively update sessions when called multiple times', (done) => {
        component.sessions$.subscribe(sessions => {
          expect(sessions.length).toBe(2);
          done();
        });
      });
    });

    it('should call SessionApiService.all when component initializes', () => {
      expect(mockSessionApiService.all).toHaveBeenCalledTimes(1);
    });
});
