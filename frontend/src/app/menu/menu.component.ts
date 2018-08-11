import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  menu$: Object;
  today = new Date();

  typeOfMeals = [
    {'id': 0, 'name': "APPETIZERS"},
    {'id': 1, 'name': "MAINS"},
    {'id': 2, 'name': "EXTRAS"}
  ];

  constructor(private data: DataService) { }

  ngOnInit() {
    this.data.getMenu().subscribe(
      data => this.menu$ = data 
    );
  }

}