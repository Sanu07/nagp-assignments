import { Component, OnInit } from '@angular/core';
import { IEmployee } from 'src/app/models/employee';
import { EmployeeService } from 'src/app/services/employee-service.service';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.scss']
})
export class EmployeeDashboardComponent implements OnInit {

  public employees: IEmployee[] = [];

  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.initializeEmployeeDetails();
  }

  initializeEmployeeDetails(): void {
    this.employeeService.getEmployees().subscribe((response: Array<IEmployee>) => {
      this.employees = response;
    });
  }

  trackByFn(index: any, employee: IEmployee): number {
    return employee.id;
 }

}
