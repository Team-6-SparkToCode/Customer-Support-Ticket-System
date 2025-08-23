-- Drop the old single-score column
ALTER TABLE tickets
DROP COLUMN csat_score;

-- Add the three new CSAT columns (1..5; validate in code)
ALTER TABLE tickets
    ADD COLUMN csat_speed_score   TINYINT NULL,
    ADD COLUMN csat_quality_score TINYINT NULL,
    ADD COLUMN csat_overall_score TINYINT NULL;
