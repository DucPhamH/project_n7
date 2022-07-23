package com.example.app_phone_store_manager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_phone_store_manager.R;
import com.example.app_phone_store_manager.dao.DaoCTHD;
import com.example.app_phone_store_manager.dao.DaoKhachHang;
import com.example.app_phone_store_manager.dao.DaoSanPham;
import com.example.app_phone_store_manager.model.ChiTietHoaDon;
import com.example.app_phone_store_manager.model.HoaDon;
import com.example.app_phone_store_manager.model.KhachHang;
import com.example.app_phone_store_manager.model.SanPham;
import com.example.app_phone_store_manager.utilities.ItemHoaDonClick;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HoaDonXuatAdapter extends RecyclerView.Adapter<HoaDonXuatAdapter.ViewHolder> implements Filterable {
    private List<HoaDon> list;
    private Context context;
    private ItemHoaDonClick itemHoaDonClick;
    private ItemHoaDonClick imgDelete;
    private DaoKhachHang daoKH;
    private DaoSanPham daoSP;
    private DaoCTHD daoCTHD;
    private Drawable drawable;
    private List<HoaDon> mListHDXold;

    public HoaDonXuatAdapter(List<HoaDon> list, Context context) {
        this.list = list;
        this.context = context;
        this.mListHDXold = list;
    }

    public void setItemHoaDonClick(ItemHoaDonClick itemHoaDonClick) {
        this.itemHoaDonClick = itemHoaDonClick;
    }

    public void setImgDelete(ItemHoaDonClick imgDelete) {
        this.imgDelete = imgDelete;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_hd_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        HoaDon hoaDon = list.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.tvMaHD.setText("Mã HD: " + hoaDon.getMaHD());
        holder.tvNgay.setText("Ngày: " + hoaDon.getNgay());

        daoCTHD = new DaoCTHD(context);
        daoKH = new DaoKhachHang(context);
        daoSP = new DaoSanPham(context);
        daoCTHD.open();
        daoSP.open();
        daoKH.open();

        if (daoKH.checkMaKH(hoaDon.getMaKH()) > 0){
            KhachHang khachHang = daoKH.getMaKH(hoaDon.getMaKH());
            holder.tvKH.setText("Khách hàng: " + khachHang.getHoTen());
        }else {
            holder.tvKH.setText("Khách hàng: null");
            holder.tvBaoHanh.setText("null");
        }

        List<ChiTietHoaDon> hoaDonList = daoCTHD.getListMaHD(hoaDon.getMaHD());
        if (hoaDonList.size() == 0){
            holder.tvGiamGia.setText("null");
            holder.tvThanhTien.setText("null");
            drawable = context.getDrawable(R.drawable.ic_kobaohanh);
            holder.imgBH.setImageDrawable(drawable);
            holder.tvBaoHanh.setText("Không bảo hành");
            holder.tvSP.setText("Sản phẩm: ngừng kinh doanh");
            holder.tvDonGia.setText("Đơn giá: null");
            holder.tvSL.setText("Số lượng: null");
        }else {
            String tenSP = "";
            String soLuong = "";
            String donGia = "";
            double tien = 0;
            int khuyenMai = 0;
            int baohanh = 0;
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            for (ChiTietHoaDon x : hoaDonList) {
                SanPham sanPham = daoSP.getMaSP(x.getMaSP());
                if (tenSP.equals("")) {
                    tenSP = sanPham.getTenSP();
                    soLuong = sanPham.getTenSP() + ": " + x.getSoLuong();
                    donGia = sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                    tien = x.getDonGia() * x.getSoLuong();
                    khuyenMai = x.getGiamGia();
                    baohanh = x.getBaoHanh();

                } else {
                    tenSP += " , " + sanPham.getTenSP();
                    soLuong += " , " + sanPham.getTenSP() + ": " + x.getSoLuong();
                    donGia += " , " + sanPham.getTenSP() + ": " + formatter.format(x.getDonGia()) + " đ";
                    tien += (x.getDonGia() * x.getSoLuong());
                }
            }
            switch (khuyenMai) {
                case 0:
                    holder.tvGiamGia.setText("Không khuyến mãi");
                    holder.tvThanhTien.setText(formatter.format(tien) + " đ");
                    break;
                case 1:
                    holder.tvGiamGia.setText("5%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.05) + " đ");
                    break;
                case 2:
                    holder.tvGiamGia.setText("10%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.1) + " đ");
                    break;
                case 3:
                    holder.tvGiamGia.setText("15%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.15) + " đ");
                    break;
                case 4:
                    holder.tvGiamGia.setText("20%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.2) + " đ");
                    break;
                case 5:
                    holder.tvGiamGia.setText("25%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.25) + " đ");
                    break;
                case 6:
                    holder.tvGiamGia.setText("30%");
                    holder.tvThanhTien.setText(formatter.format(tien - tien * 0.3) + " đ");
                    break;
            }
            switch (baohanh) {
                case 0:
                    drawable = context.getDrawable(R.drawable.ic_baohanh6t);
                    holder.imgBH.setImageDrawable(drawable);
                    holder.tvBaoHanh.setText("6 tháng BH");
                    break;
                case 1:
                    drawable = context.getDrawable(R.drawable.ic_baohanh12t);
                    holder.imgBH.setImageDrawable(drawable);
                    holder.tvBaoHanh.setText("12 tháng BH");
                    break;
                default:
                    drawable = context.getDrawable(R.drawable.ic_kobaohanh);
                    holder.imgBH.setImageDrawable(drawable);
                    holder.tvBaoHanh.setText("Không bảo hành");
                    break;
            }
            holder.tvSP.setText("Sản phẩm: " + tenSP);
            holder.tvDonGia.setText("Đơn giá: " +donGia);
            holder.tvSL.setText("Số lượng: " + soLuong);
        }
        daoCTHD.close();
        daoKH.close();
        daoKH.close();
        switch (hoaDon.getTrangThai()) {
            case 0:
                holder.tvTTHDX.setText("Đã được xử lý");
                holder.tvTTHDX.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case 1:
                holder.tvTTHDX.setText("Vận chuyển");
                holder.tvTTHDX.setTextColor(context.getResources().getColor(R.color.teal_200));
                break;
            case 2:
                holder.tvTTHDX.setText("Hoàn thành");
                holder.tvTTHDX.setTextColor(context.getResources().getColor(R.color.green));
                break;
            default:
                holder.tvTTHDX.setText("Đang xử lý");
                holder.tvTTHDX.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHoaDonClick.ItemClick(hoaDon);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDelete.ItemClick(hoaDon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void filter(List<HoaDon> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String serHDX = constraint.toString();
                if (serHDX.isEmpty()) {
                    list = mListHDXold;
                } else {
                    List<HoaDon> listHDX = new ArrayList<>();
                    for (HoaDon hoaDonXuat : mListHDXold) {
                        if (hoaDonXuat.getMaHD().toLowerCase().contains(serHDX.toLowerCase()) ||
                                hoaDonXuat.getNgay().toLowerCase().contains(serHDX.toLowerCase())) {
                            listHDX.add(hoaDonXuat);
                        }
                    }
                    list = listHDX;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<HoaDon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBH, imgDelete;
        TextView tvMaHD, tvKH, tvSP, tvNgay, tvSL, tvDonGia, tvGiamGia, tvThanhTien, tvTTHDX, tvBaoHanh;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgBH = itemView.findViewById(R.id.imgBaoHanh);
            imgDelete = itemView.findViewById(R.id.imgDeleteHDXItem);
            tvMaHD = itemView.findViewById(R.id.tvMaHDXItem);
            tvKH = itemView.findViewById(R.id.tvTenKHXItem);
            tvSP = itemView.findViewById(R.id.tvTenSPXItem);
            tvNgay = itemView.findViewById(R.id.tvNgayXuatItem);
            tvSL = itemView.findViewById(R.id.tvSLXuatItem);
            tvDonGia = itemView.findViewById(R.id.tvDonGiaXItem);
            tvGiamGia = itemView.findViewById(R.id.tvKMXItem);
            tvBaoHanh = itemView.findViewById(R.id.tvBaoHanh);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTienXItem);
            tvTTHDX = itemView.findViewById(R.id.tvTTHDXItem);
        }
    }
}