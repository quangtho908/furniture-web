package Services;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

public class UploadService {

    private CloudinaryService cloudService;

    public UploadService() {
        cloudService = new CloudinaryService();
    }

    public String handleFile(String rootPath, Collection<Part> parts, String folderUpload) throws IOException {
        File folder = new File(rootPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String file = null;
        for (Part part : parts) {
            if(file == null) {
                file = rootPath + File.separator +  Instant.now().toEpochMilli() + "_" + new Random().nextInt() + "." + getExtension(part);
            }
            part.write(file);
        }

        File fileUpload = new File(file);

        String result = this.cloudService.upload(fileUpload, folderUpload);

        fileUpload.delete();

        return  result;

    }

    private String getExtension(Part part) {
        String submittedFile = part.getSubmittedFileName();

        return  submittedFile.substring(submittedFile.lastIndexOf(".") + 1);
    }
}