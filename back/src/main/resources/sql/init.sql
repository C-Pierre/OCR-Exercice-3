-- USERS
INSERT IGNORE INTO test.users(first_name, last_name, admin, email, password)
VALUES
    ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
    ('John', 'Doe', false, 'john.doe@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

-- TEACHERS
INSERT IGNORE INTO test.teachers(first_name, last_name)
VALUES
    ('Julien', 'Moreau'),
    ('Claire', 'Bernard');

-- SESSIONS
INSERT IGNORE INTO test.sessions(name, date, description, teacher_id)
SELECT 'Morning Yoga', '2026-06-15', 'Relaxing morning yoga session', id
FROM test.teachers
WHERE last_name='Moreau';

INSERT IGNORE INTO test.sessions(name, date, description, teacher_id)
SELECT 'Power Yoga', '2026-06-16', 'Intense yoga session for advanced level', id
FROM test.teachers
WHERE last_name='Bernard';

-- PARTICIPANTS
INSERT INTO test.participate (session_id, user_id)
SELECT s.id, u.id
FROM test.sessions s JOIN test.users u ON u.last_name = 'Admin'
WHERE s.name = 'Morning Yoga'
ON DUPLICATE KEY UPDATE session_id = session_id;

INSERT INTO test.participate (session_id, user_id)
SELECT s.id, u.id
FROM test.sessions s JOIN test.users u ON u.last_name = 'Doe'
WHERE s.name IN ('Morning Yoga', 'Power Stretch')
ON DUPLICATE KEY UPDATE session_id = session_id;
