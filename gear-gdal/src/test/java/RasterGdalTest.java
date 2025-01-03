import org.gdal.gdal.Band;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdal.Dataset;
import org.gdal.gdalconst.gdalconstConstants;


/**
 * @author xming
 * @createTime 2022/5/7 16:37
 * @description
 */
public class RasterGdalTest {
    private static final String FILE_PATH = "F:/project-data/raster/beijingtiff4326/beijing_5m.tif";
    static {
        // 注册所有的驱动
        gdal.AllRegister();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption("SHAPE_ENCODING","");
    }

    public static void main(String[] args) {
        // 读取影像数据
        Dataset dataset = gdal.Open(FILE_PATH, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            System.out.println("read fail!");
            return;
        }

        //  providing various methods for a format specific driver.
        Driver driver = dataset.GetDriver();
        System.out.println("driver name : " + driver.getLongName());

        // 读取影像信息
        int xSize = dataset.getRasterXSize();
        int ySzie = dataset.getRasterYSize();
        int rasterCount = dataset.getRasterCount();
        System.out.println("dataset xSize:" + xSize + ", ySzie = " + ySzie + ", rasterCount = " + rasterCount);

        Band band = dataset.GetRasterBand(1);
        //the data type of the band.
        int type = band.GetRasterDataType();
        System.out.println("data type = " + type + ", " + (type == gdalconstConstants.GDT_Byte));

        //Frees the native resource associated to a Dataset object and close the file.
        dataset.delete();

        gdal.GDALDestroyDriverManager();
    }
}
