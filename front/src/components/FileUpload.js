import { useState } from "react";

const FileUpload = ({ uploadUrl, outputFileName }) => {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState("");
    const [downloadLink, setDownloadLink] = useState("");

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async (e) => {
        e.preventDefault();

        if (!file) {
            setMessage("Please select a file first.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch(uploadUrl, {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                const text = await response.text();
                setMessage(text);
                setDownloadLink(`http://localhost:8081/download?fileName=${outputFileName}`);
            } else {
                setMessage("Failed to upload file.");
            }
        } catch (error) {
            console.error("Error:", error);
            setMessage("An error occurred during upload.");
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h2>Upload File</h2>
            <form onSubmit={handleUpload}>
                <input type="file" onChange={handleFileChange} />
                <button type="submit">Upload</button>
            </form>
            {message && <p>{message}</p>}
            {downloadLink && (
                <a href={downloadLink} download>
                    <button>Download Result</button>
                </a>
            )}
        </div>
    );
};

export default FileUpload;
