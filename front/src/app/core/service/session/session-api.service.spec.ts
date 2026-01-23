import { expect } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import { Session } from '../../models/session/session.interface';
import { provideHttpClient } from '@angular/common/http';
import { SessionApiService } from './session-api.service';
import { environment } from 'src/environments/environment';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/session`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const session: Session[] = [{
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [],
      date: new Date('2026-01-10T10:00:00Z')
    }];

    service.all().subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');

    req.flush(session);
  });

  it('should get session detail', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.detail('10').subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(`${baseUrl}/10`);
    expect(req.request.method).toBe('GET');

    req.flush(session);
  });

  it('should delete a session', () => {
    service.delete('5').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/5`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });

  it('should create a session', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.create(session).subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(session);

    req.flush(session);
  });

  it('should update a session', () => {
    const session: Session = {
      id: 1,
      name: 'Math',
      description: 'Cours de mathématiques',
      teacherId: 42,
      users: [],
      date: new Date('2026-01-10T10:00:00Z')
    };

    service.update('30', session).subscribe(result => {
      expect(result).toEqual(session);
    });

    const req = httpMock.expectOne(`${baseUrl}/30`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(session);

    req.flush(session);
  });

  it('should participate in session', () => {
    service.participate('1', '99').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/99`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unParticipate from session', () => {
    service.unParticipate('1', '99').subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/participate/99`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  describe('Integration tests (session flow)', () => {
    it('should create, participate, fetch and delete session flow', () => {
      const session: Session = {
        id: 1,
        name: 'Physique',
        description: 'Cours de physique',
        teacherId: 1,
        users: [],
        date: new Date('2026-01-15T14:00:00Z')
      };

      service.create(session).subscribe(result => {
        expect(result).toEqual(session);
      });
      const createReq = httpMock.expectOne(baseUrl);
      expect(createReq.request.method).toBe('POST');
      createReq.flush(session);

      service.participate('1', '99').subscribe(res => expect(res).toBeUndefined());
      const participateReq = httpMock.expectOne(`${baseUrl}/1/participate/99`);
      expect(participateReq.request.method).toBe('POST');
      participateReq.flush(null);

      service.detail('1').subscribe(res => {
        expect(res).toEqual(session);
      });
      const detailReq = httpMock.expectOne(`${baseUrl}/1`);
      expect(detailReq.request.method).toBe('GET');
      detailReq.flush(session);

      service.delete('1').subscribe(res => expect(res).toBeUndefined());
      const deleteReq = httpMock.expectOne(`${baseUrl}/1`);
      expect(deleteReq.request.method).toBe('DELETE');
      deleteReq.flush(null);
    });

    it('should handle errors correctly', () => {
      service.detail('999').subscribe({
        next: () => fail('should not succeed'),
        error: err => expect(err.status).toBe(404)
      });

      const req = httpMock.expectOne(`${baseUrl}/999`);
      req.flush({ message: 'Not Found' }, { status: 404, statusText: 'Not Found' });
    });
  });
});
