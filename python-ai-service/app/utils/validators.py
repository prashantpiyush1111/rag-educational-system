def validate_question(question: str) -> bool:
    if not question or not question.strip():
        return False
    if len(question) > 1000:
        return False
    return True

def validate_file_extension(filename: str, allowed_extensions: list[str]) -> bool:
    ext = "." + filename.split(".")[-1].lower()
    return ext in allowed_extensions