import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { UserService } from './user.service';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from '../../models/user/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/user`;

  const mockUser: User = {
    id: 1,
    email: 'jdoe@example.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    password: 'password123',
    createdAt: new Date('2026-01-09T10:00:00Z'),
    updatedAt: new Date('2026-01-09T12:00:00Z')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    service.getById('1').subscribe(result => {
      expect(result).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('GET');

    req.flush(mockUser);
  });

  it('should delete user', () => {
    service.delete('1').subscribe(result => {
      expect(result).toBeNull();
    });

    const req = httpMock.expectOne(`${baseUrl}/1`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });

  describe('Integration tests (user flow)', () => {

    it('should get user and then delete it', () => {
      service.getById('1').subscribe(result => {
        expect(result).toEqual(mockUser);
      });
      const getReq = httpMock.expectOne(`${baseUrl}/1`);
      expect(getReq.request.method).toBe('GET');
      getReq.flush(mockUser);

      service.delete('1').subscribe(result => {
        expect(result).toBeNull();
      });
      const deleteReq = httpMock.expectOne(`${baseUrl}/1`);
      expect(deleteReq.request.method).toBe('DELETE');
      deleteReq.flush(null);
    });

    it('should handle error when getting non-existent user', () => {
      service.getById('999').subscribe({
        next: () => fail('should not succeed'),
        error: err => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${baseUrl}/999`);
      req.flush({ message: 'Not Found' }, { status: 404, statusText: 'Not Found' });
    });

    it('should handle error when deleting non-existent user', () => {
      service.delete('999').subscribe({
        next: () => fail('should not succeed'),
        error: err => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${baseUrl}/999`);
      req.flush({ message: 'Not Found' }, { status: 404, statusText: 'Not Found' });
    });

  });
});
