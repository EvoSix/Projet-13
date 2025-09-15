// src/app/core/services/toast.service.ts
import { Injectable, NgZone } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class ToastService {
  constructor(private snackBar: MatSnackBar, private zone: NgZone) {}

  showError(message: string): void {
    this.zone.run(() => {
      this.snackBar.open(message, 'Fermer', {
        duration: 2500,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: ['error-snackbar'],
      });
    });
  }
  show(message: string): void {
    this.zone.run(() => {
      this.snackBar.open(message, 'Fermer', {
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        duration: 1500,
        panelClass: ['success-snackbar'],
      });
    });
  }
}
