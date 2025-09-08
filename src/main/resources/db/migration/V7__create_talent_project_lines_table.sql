CREATE TABLE talent_project_lines (
    talent_id BIGINT NOT NULL,
    project_line VARCHAR(255) NOT NULL,
    CONSTRAINT fk_talent_project_lines_on_talent FOREIGN KEY (talent_id) REFERENCES Talents (id)
);
