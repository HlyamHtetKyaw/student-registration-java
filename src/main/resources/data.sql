-- STUDENT_AFFAIR users
INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'affair1@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'STUDENT_AFFAIR'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'affair1@example.com');

INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'affair2@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'STUDENT_AFFAIR'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'affair2@example.com');

-- FINANCE users
INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'finance1@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'FINANCE'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'finance1@example.com');

INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'finance2@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'FINANCE'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'finance2@example.com');

-- DEAN users
INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'dean1@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'DEAN'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'dean1@example.com');

INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'dean2@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'DEAN'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'dean2@example.com');

-- STUDENT users
INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'student1@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'STUDENT'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'student1@example.com');

INSERT INTO users (email, password, role_id, created_at, updated_at)
SELECT 'student2@example.com', '$2a$10$Z0n34nYDjtWUoVxL..3Yp.W4OlFRo0gYUJ.hPvEtTQMuZHxqaI.h2', r.id, NOW(), NOW()
FROM roles r
WHERE r.name = 'STUDENT'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'student2@example.com');
  
-- Profile for affair1@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'Aung Aung', 'အောင် အောင်', '12/OUKANA(N)123456',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'affair1@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

-- Profile for affair2@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'Mya Mya', 'မြ မြ', '5/MAKANA(N)654321',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'affair2@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

  
-- Profile for finance1@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'Hla Hla', 'လှ လှ', '7/YAKANA(N)111222',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'finance1@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

-- Profile for finance2@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'Ko Ko', 'ကို ကို', '8/TAKANA(N)333444',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'finance2@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

  
-- Profile for dean1@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'U Tun', 'ဦး ထွန်း', '1/LAKANA(N)999888',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'dean1@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

-- Profile for dean2@example.com
INSERT INTO profiles (name_eng, name_mm, nrc, user_id, created_at, updated_at)
SELECT 'Daw Hnin', 'ဒေါ် နှင်း', '3/PAKANA(N)777666',
       u.id, NOW(), NOW()
FROM users u
WHERE u.email = 'dean2@example.com'
  AND NOT EXISTS (SELECT 1 FROM profiles WHERE user_id = u.id);

