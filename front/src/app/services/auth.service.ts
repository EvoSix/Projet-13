import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { RegisterRequest } from '../interfaces/registerRequest';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';
import { LoginRequest } from '../interfaces/LoginRequest';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = environment.baseUrl;
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  isLoggedIn$ = this.isLoggedInSubject.asObservable();
  constructor() {}

  validateToken(): Observable<boolean> {
    return this.http.get(`${this.apiUrl}users/me`).pipe(
      map(() => {
        this.isLoggedInSubject.next(true);
        return true;
      }),
      catchError(() => {
        this.logout(); // on nettoie le token
        return of(false);
      })
    );
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}auth/register`, registerRequest);
  }
  login(payload: LoginRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}auth/login`, payload);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isLoggedInSubject.next(false);
  }
  setLoggedIn(token: string): void {
    localStorage.setItem('token', token);
    this.isLoggedInSubject.next(true);
  }
}
