import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  promotions = [
    {
      title: 'Ưu đãi mùa hè',
      description: 'Giảm 20% cho tất cả liệu trình chăm sóc da đến hết tháng 8!'
    },
    {
      title: 'Combo đặc biệt',
      description: 'Gói “Căng bóng + Cấy trắng” chỉ 750.000đ (tiết kiệm 100.000đ)'
    },
    {
      title: 'Quà tặng thành viên',
      description: 'Khách hàng thân thiết nhận 1 buổi miễn phí mỗi 5 lần đặt hẹn!'
    }
  ];

  currentIndex = 0;
  intervalId: any;

  ngOnInit() {
    this.intervalId = setInterval(() => this.changePromo(1), 5000);
  }

  changePromo(direction: number) {
    this.currentIndex = (this.currentIndex + direction + this.promotions.length) % this.promotions.length;
  }

  ngOnDestroy() {
    if (this.intervalId) clearInterval(this.intervalId);
  }

}
