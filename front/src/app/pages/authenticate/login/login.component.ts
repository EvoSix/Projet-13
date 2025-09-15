import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { HeaderComponent } from '../../../components/Layout/header/header.component';
import { MatIconModule } from '@angular/material/icon';
import { ToastService } from 'src/app/services/toast.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,

    MatIconModule,
    HeaderComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  loading = false;
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private toastService: ToastService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      identifier: ['', Validators.required], // email ou username
      password: ['', Validators.required],
    });
  }
  private destroy$ = new Subject<void>();
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    this.loading = true;
    const payload = {
      email: this.f['identifier'].value,
      password: this.f['password'].value,
    };
    this.authService.login(payload).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.loading = false;
        localStorage.setItem('token', response.token ?? '');
        this.authService.setLoggedIn(response.token ?? '');
        this.toastService.show('Connexion reussie');
        this.router.navigateByUrl('/chats');
      },
      error: () => {
        this.loading = false;
      }
    });
  }


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

}
